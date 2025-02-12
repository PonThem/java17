package com.valuelab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.valuelab.entity.MstUser;
import com.valuelab.form.DeleteUserForm;
import com.valuelab.repository.MstUsersRepository;
import com.valuelab.common.exception.ExclusiveException;

@Service
public class DeleteUserServiceImpl implements DeleteUserService {
    @Value("${cognito.userPoolId}")
    private String userPoolId;

    @Autowired
    private AWSCognitoIdentityProvider awsCognitoIdentityProviderAdmin;

    @Autowired
    private MstUsersRepository mstUsersRepository;

    /**
     * Deletes a user from DB and Cognito
     * 
     * @param DeleteUserForm form
     */
    public void delete(DeleteUserForm form) {
        // delete user from DB
        MstUser user = new MstUser();
        user.setUpdatedDatetime(form.getUpdatedDatetime());
        user.setUuid(form.getUuid());

        int deleteCount = mstUsersRepository.deleteMstUser(user);

        if (deleteCount == 0) {
            throw new ExclusiveException();
        }

        MstUser deletedUser = mstUsersRepository.selectUserDetailsForDeletedUser(form.getUuid());

        // remove user to Cognito
        // Create an AdminDeleteUserRequest
        AdminDeleteUserRequest deleteUserRequest = new AdminDeleteUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(deletedUser.getUserId());

        // Call the adminDeleteUser method
        awsCognitoIdentityProviderAdmin.adminDeleteUser(deleteUserRequest);
    }
}
