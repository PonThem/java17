package com.valuelab.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.valuelab.dto.UpdateUserDto;
import com.valuelab.entity.MstUser;
import com.valuelab.form.UpdateUserForm;
import com.valuelab.repository.MstUsersRepository;
import com.valuelab.common.exception.DuplicatedUserException;
import com.valuelab.common.exception.ExclusiveException;

@Service
class UpdateUserServiceImpl implements UpdateUserService {
    private static final String EMAIL_ATTRIBUTE_NAME = "email";

    @Value("${cognito.userPoolId}")
    private String userPoolId;

    @Autowired
    private AWSCognitoIdentityProvider awsCognitoIdentityProviderAdmin;

    @Autowired
    private MstUsersRepository mstUsersRepository;

    @Autowired
    private LoginUserDetailsService loginUserDetailsService;

    /**
     * Updates a user in DB and Cognito
     * 1 check the count of user in DB, if >1, throw exception
     * 2 update the user in DB
     * 3 add user to Cognito
     * 
     * @param updateUserForm UpdateUserForm
     * @return UpdateUserDto
     */
    @Transactional
    public UpdateUserDto update(UpdateUserForm updateUserForm) {
        UpdateUserDto dto = new UpdateUserDto();

        try {
            // check user count, if >1 , throw exception
            int userCount = mstUsersRepository.checkUserCountByUuid(updateUserForm.getUuid());
            if (userCount > 1) {
                throw new DuplicatedUserException();
            } else if (userCount == 0) {
                throw new ExclusiveException("user not found");
            }
            // get old user info
            MstUser oldUserInDb = mstUsersRepository.selectUserDetails(updateUserForm.getUuid());

            // update user at DB
            LocalDateTime curDateTime = LocalDateTime.now(ZoneId.of("UTC"));
            MstUser user = new MstUser();
            user.setUserId(updateUserForm.getUserId());
            user.setMailAddress(updateUserForm.getMailAddress());
            user.setUserName(updateUserForm.getUserName());
            user.setUpdatedDatetime(updateUserForm.getUpdatedDatetime());
            user.setUserType(updateUserForm.getUserType());
            user.setUuid(updateUserForm.getUuid());
            user.setUpdatedUserPk(loginUserDetailsService.getUserInfo().getUserPk().intValue());
            int updateCount = mstUsersRepository.updateMstUser(user.getUserId(), user.getUserName(), user.getUserType(),
                    user.getMailAddress(), user.getUuid(), user.getUpdatedDatetime(), user.getUpdatedUserPk(),
                    curDateTime);

            if (updateCount == 0) {
                System.out.println("update failed");
                throw new ExclusiveException("user update failed");
            }

            // if the user_id is not changed, update user at Cognito
            // if the user_id is changed, add user into Cognito by new_user_id and delete
            // the old user_id
            if (!oldUserInDb.getUserId().equals(updateUserForm.getUserId())) {
                // check if the new user_id is in Cognito
                AdminGetUserRequest getUserRequest = new AdminGetUserRequest()
                        .withUserPoolId(userPoolId)
                        .withUsername(updateUserForm.getUserId());
                AdminGetUserResult adminGetUserResponse = null;
                try {
                    // check if user exist in Cognito
                    adminGetUserResponse = awsCognitoIdentityProviderAdmin.adminGetUser(getUserRequest);
                } catch (UserNotFoundException e) {
                    // if not, do nothing
                    System.out.println("User not found in Cognito");
                }
                // if exist
                if (adminGetUserResponse != null) {
                    // update user at Cognito
                    List<AttributeType> userAttributes = new ArrayList<>();
                    userAttributes.add(new AttributeType()
                            .withName(EMAIL_ATTRIBUTE_NAME)
                            .withValue(updateUserForm.getMailAddress()));
                    userAttributes.add(new AttributeType()
                            .withName("email_verified")
                            .withValue("true"));

                    // in Cognito the username is user_id
                    AdminUpdateUserAttributesRequest userRequest = new AdminUpdateUserAttributesRequest()
                            .withUserPoolId(userPoolId)
                            .withUsername(updateUserForm.getUserId())
                            .withUserAttributes(userAttributes);

                    awsCognitoIdentityProviderAdmin.adminUpdateUserAttributes(userRequest);
                } else {
                    // add user to Cognito
                    List<AttributeType> userAttributes = new ArrayList<>();
                    userAttributes.add(new AttributeType()
                            .withName(EMAIL_ATTRIBUTE_NAME)
                            .withValue(updateUserForm.getMailAddress()));
                    userAttributes.add(new AttributeType()
                            .withName("custom:uuid")
                            .withValue(updateUserForm.getUuid()));

                    // Set temporary password
                    String temporaryPassword = generateRandomString();

                    // in Cognito the username is user_id
                    AdminCreateUserRequest userRequest = new AdminCreateUserRequest()
                            .withUserPoolId(userPoolId)
                            .withUsername(updateUserForm.getUserId())
                            .withUserAttributes(userAttributes)
                            .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                            .withForceAliasCreation(Boolean.FALSE)
                            .withTemporaryPassword(temporaryPassword);

                    awsCognitoIdentityProviderAdmin.adminCreateUser(userRequest);
                }

                // delete old user id
                // remove user to Cognito
                // Create an AdminDeleteUserRequest
                AdminDeleteUserRequest deleteUserRequest = new AdminDeleteUserRequest()
                        .withUserPoolId(userPoolId)
                        .withUsername(oldUserInDb.getUserId());

                // Call the adminDeleteUser method
                awsCognitoIdentityProviderAdmin.adminDeleteUser(deleteUserRequest);

            } else {
                // update user at Cognito
                List<AttributeType> userAttributes = new ArrayList<>();
                userAttributes.add(new AttributeType()
                        .withName(EMAIL_ATTRIBUTE_NAME)
                        .withValue(updateUserForm.getMailAddress()));
                userAttributes.add(new AttributeType()
                        .withName("email_verified")
                        .withValue("true"));

                // in Cognito the username is user_id
                AdminUpdateUserAttributesRequest userRequest = new AdminUpdateUserAttributesRequest()
                        .withUserPoolId(userPoolId)
                        .withUsername(updateUserForm.getUserId())
                        .withUserAttributes(userAttributes);

                awsCognitoIdentityProviderAdmin.adminUpdateUserAttributes(userRequest);
            }

            dto.setUuid(updateUserForm.getUuid());
            dto.setUpdatedDatetime(curDateTime);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return dto;
    }

    private String generateRandomString() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int PASSWORD_LENGTH = 8;
        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
