package com.valuelab.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.valuelab.dto.AddUserDto;
import com.valuelab.form.AddUserForm;
import com.valuelab.entity.MstUser;
import com.valuelab.repository.MstUsersRepository;
import com.valuelab.common.exception.DuplicatedUserException;
import com.valuelab.common.exception.ExclusiveException;

@Service
public class AddUserServiceImpl implements AddUserService {

        @Value("${cognito.userPoolId}")
        private String userPoolId;

        @Autowired
        private AWSCognitoIdentityProvider awsCognitoIdentityProviderAdmin;

        @Autowired
        private MstUsersRepository mstUsersRepository;

        @Autowired
        private LoginUserDetailsService loginUserDetailsService;

        /**
         * Registers a new user into DB and Cognito
         * 1 check the user_id is in Cognito or not
         * 2 if exist, check the status and enable
         * 2-1 if confirmed and enabled, throw exception
         * 2-2 if not, select DB user count by user_id
         * 2-2-1 if >1, throw exception
         * 2-2-2 if 1, update DB user
         * 2-2-2-1 if success, add user with resend message to Cognito
         * 2-2-3 if 0, throw ExclusiveException
         * 3 if not
         * 4 add user to DB
         * 5 if success , add user to Cognito
         * 6 if success , return
         * 7 if fail , auto rollback and throw error
         * 
         * @param addUserForm AddUserForm
         * @return AddUserDto
         */
        @Transactional
        public AddUserDto register(AddUserForm addUserForm) {
                AddUserDto dto = new AddUserDto();
                // 1 check the user_id is in Cognito or not
                AdminGetUserRequest getUserRequest = new AdminGetUserRequest()
                                .withUserPoolId(userPoolId)
                                .withUsername(addUserForm.getUserId());
                AdminGetUserResult adminGetUserResponse = null;
                try {
                        // check if user exist in Cognito
                        adminGetUserResponse = awsCognitoIdentityProviderAdmin.adminGetUser(getUserRequest);
                } catch (UserNotFoundException e) {
                        // if not, do nothing
                        System.out.println("User not found in Cognito");
                }

                if (adminGetUserResponse != null) {
                        // 2 if exist, check the status and enable
                        // check if user status is confirmed and enabled
                        if (adminGetUserResponse.getUserStatus().equals("CONFIRMED")) {
                                List<MstUser> dbUserList = mstUsersRepository
                                                .getMstUserByUserId(addUserForm.getUserId());
                                if (dbUserList.size() == 0) {
                                        // if in db, not in Cognito, then delete this user
                                        // Create an AdminDeleteUserRequest
                                        AdminDeleteUserRequest deleteUserRequest = new AdminDeleteUserRequest()
                                                        .withUserPoolId(userPoolId)
                                                        .withUsername(addUserForm.getUserId());

                                        // Call the adminDeleteUser method
                                        awsCognitoIdentityProviderAdmin.adminDeleteUser(deleteUserRequest);
                                } else {
                                        // 2-1 if confirmed and enabled, throw exception
                                        throw new DuplicatedUserException();
                                }
                        } else {
                                // 2-2 if not, select DB user count by user_id
                                List<MstUser> dbUserList = mstUsersRepository
                                                .getMstUserByUserId(addUserForm.getUserId());

                                if (dbUserList.size() > 1) {
                                        // 2-2-1 if >1, throw exception
                                        throw new DuplicatedUserException();
                                } else if (dbUserList.size() == 1) {
                                        // 2-2-2 if only one , update DB user
                                        return updateUser(addUserForm, dbUserList.get(0));
                                } else {
                                        // 2-2-3 if 0, throw exception
                                        // throw new ExclusiveException();

                                        // if in db, not in Cognito, then delete this user
                                        // Create an AdminDeleteUserRequest
                                        AdminDeleteUserRequest deleteUserRequest = new AdminDeleteUserRequest()
                                                        .withUserPoolId(userPoolId)
                                                        .withUsername(addUserForm.getUserId());

                                        // Call the adminDeleteUser method
                                        awsCognitoIdentityProviderAdmin.adminDeleteUser(deleteUserRequest);
                                }
                        }
                }

                // add user to DB
                LocalDateTime curDateTime = LocalDateTime.now(ZoneId.of("UTC"));
                MstUser loginUser = loginUserDetailsService.getUserInfo();

                MstUser user = new MstUser();
                user.setUserId(addUserForm.getUserId());
                user.setMailAddress(addUserForm.getMailAddress());
                user.setUserName(addUserForm.getUserName());
                user.setCreatedDatetime(curDateTime);
                user.setUserType(addUserForm.getUserType());
                user.setCreatedUserPk(loginUser.getUserPk().intValue());
                user.setUpdatedDatetime(curDateTime);
                user.setUpdatedUserPk(loginUser.getUserPk().intValue());
                // generate uuid
                String uuid = UUID.randomUUID().toString();
                user.setUuid(uuid);

                int insertCount = mstUsersRepository.insertMstUser(user);
                System.out.println("insertCount = " + insertCount);

                if (insertCount != 1) {
                        // if fail, auto rollback and throw error
                        throw new ExclusiveException();
                }

                // add user to Cognito
                List<AttributeType> userAttributes = new ArrayList<>();
                userAttributes.add(new AttributeType()
                                .withName("email")
                                .withValue(addUserForm.getMailAddress()));
                userAttributes.add(new AttributeType()
                                .withName("custom:uuid")
                                .withValue(uuid));

                // Set temporary password
                String temporaryPassword = generateRandomString();

                // in Cognito the username is user_id
                AdminCreateUserRequest userRequest = new AdminCreateUserRequest()
                                .withUserPoolId(userPoolId)
                                .withUsername(addUserForm.getUserId())
                                .withUserAttributes(userAttributes)
                                .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                                .withForceAliasCreation(Boolean.FALSE)
                                .withTemporaryPassword(temporaryPassword);

                awsCognitoIdentityProviderAdmin.adminCreateUser(userRequest);

                dto.setUuid(uuid);
                dto.setUpdatedDatetime(curDateTime);

                return dto;
        }

        private AddUserDto updateUser(AddUserForm addUserForm, MstUser dbUser) {
                AddUserDto dto = new AddUserDto();

                // 2-2-2 update DB user
                MstUser loginUser = loginUserDetailsService.getUserInfo();

                MstUser user = new MstUser();
                user.setUserId(addUserForm.getUserId());
                user.setMailAddress(addUserForm.getMailAddress());
                user.setUserName(addUserForm.getUserName());
                user.setUserType(addUserForm.getUserType());

                user.setUuid(dbUser.getUuid());
                user.setUpdatedDatetime(dbUser.getUpdatedDatetime());
                user.setUpdatedUserPk(loginUser.getUserPk().intValue());

                LocalDateTime curDateTime = LocalDateTime.now(ZoneId.of("UTC"));
                mstUsersRepository.updateMstUser(user.getUserId(), user.getUserName(), user.getUserType(),
                                user.getMailAddress(), user.getUuid(), user.getUpdatedDatetime(),
                                user.getUpdatedUserPk(), curDateTime);

                // 2-2-2-1 if success, add user with resend message to Cognito
                List<AttributeType> userAttributes = new ArrayList<>();

                userAttributes.add(new AttributeType()
                                .withName("email")
                                .withValue(addUserForm.getMailAddress()));
                userAttributes.add(new AttributeType()
                                .withName("custom:uuid")
                                .withValue(dbUser.getUuid()));

                // Set temporary password
                String temporaryPassword = generateRandomString();

                AdminCreateUserRequest userRequest = new AdminCreateUserRequest()
                                .withUserPoolId(userPoolId)
                                .withUsername(addUserForm.getUserId())
                                .withUserAttributes(userAttributes)
                                .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                                .withForceAliasCreation(Boolean.FALSE)
                                .withMessageAction("RESEND")
                                .withTemporaryPassword(temporaryPassword);

                awsCognitoIdentityProviderAdmin.adminCreateUser(userRequest);

                dto.setUuid(dbUser.getUuid());
                dto.setUpdatedDatetime(curDateTime);

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
