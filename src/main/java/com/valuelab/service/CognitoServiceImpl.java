package com.valuelab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserResult;
import com.amazonaws.services.cognitoidp.model.AdminEnableUserRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.AdminEnableUserResult;

import com.valuelab.entity.User;

@Service
public class CognitoServiceImpl implements CognitoService {

        @Value("${cognito.userPoolId}")
        private String userPoolId;

        @Value("${cognito.clientId}")
        private String clientId;

        @Autowired
        private AWSCognitoIdentityProvider awsCognitoIdentityProviderAdmin;

        @Autowired
        private AWSCognitoIdentityProvider awsCognitoIdentityProviderBasic;

        /**
         * auto generate password register
         * 
         * @param username The username of the user.
         * @param email    The email of the user.
         * @return An AuthResultForm object containing the status and message of the
         *         login attempt.
         */
        @Transactional
        public AdminCreateUserResult adminCreateUser(String username, String email) {
                List<AttributeType> userAttributes = new ArrayList<>();
                userAttributes.add(new AttributeType()
                                .withName("email")
                                .withValue(email));

                AdminCreateUserRequest userRequest = new AdminCreateUserRequest()
                                .withUserPoolId(userPoolId)
                                .withUsername(username)
                                .withUserAttributes(userAttributes)
                                .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                                .withForceAliasCreation(Boolean.FALSE);

                AdminCreateUserResult createUserResult = null;

                createUserResult = awsCognitoIdentityProviderAdmin.adminCreateUser(userRequest);

                return createUserResult;
        }

        /**
         * Registers a new user in the AWS Cognito user pool.
         * 
         * @param username The desired username for the new user.
         * @param email    The email address of the new user.
         * @param password The password for the new user.
         * @return A SignUpResult object containing the result of the registration
         *         process.
         */
        @Transactional
        public SignUpResult register(String username, String email, String password, String role) {
                // insert user into table user
                SignUpResult result = null;

                // Generate a unique UUID for the custom:uuid attribute
                String uuid = UUID.randomUUID().toString();
                User userEntity = new User();
                userEntity.setUsername(username);
                userEntity.setEmail(email);
                userEntity.setCognitoSubId("");
                userEntity.setRole(role);
                userEntity.setCustomUuid(uuid);

                SignUpRequest signUpRequest = new SignUpRequest()
                                .withClientId(clientId)
                                .withUsername(username)
                                .withPassword(password)
                                .withUserAttributes(
                                                new AttributeType()
                                                                .withName("email")
                                                                .withValue(email),
                                                new AttributeType()
                                                                .withName("custom:uuid")
                                                                .withValue(uuid));

                result = awsCognitoIdentityProviderBasic.signUp(signUpRequest);

                return result;
        }

        /**
         * enable user
         * 
         * @param username The username of the user.
         * @return An AuthResultForm object containing the status and message of the
         *         login attempt.
         */
        public AdminEnableUserResult enableUser(String username) {
                AdminEnableUserResult result = null;

                // Enable a user
                AdminEnableUserRequest enableRequest = new AdminEnableUserRequest()
                                .withUserPoolId(userPoolId)
                                .withUsername(username);
                result = awsCognitoIdentityProviderAdmin.adminEnableUser(enableRequest);

                return result;
        }

        /**
         * dis user
         * 
         * @param username The username of the user.
         * @return An AuthResultForm object containing the status and message of the
         *         login attempt.
         */
        public AdminDisableUserResult disableUser(String username) {
                AdminDisableUserResult result = null;

                // Disable a user
                AdminDisableUserRequest disableRequest = new AdminDisableUserRequest()
                                .withUserPoolId(userPoolId)
                                .withUsername(username);
                result = awsCognitoIdentityProviderAdmin.adminDisableUser(disableRequest);

                return result;
        }
}
