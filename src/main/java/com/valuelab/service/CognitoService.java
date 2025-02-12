package com.valuelab.service;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserResult;
import com.amazonaws.services.cognitoidp.model.AdminEnableUserResult;
import com.amazonaws.services.cognitoidp.model.SignUpResult;

public interface CognitoService {

  /**
   * Registers a new user in the AWS Cognito user pool.
   * 
   * @param username The desired username for the new user.
   * @param email    The email address of the new user.
   * @param password The password for the new user.
   * @return A SignUpResult object containing the result of the registration
   *         process.
   */
  SignUpResult register(String username, String email, String password, String role);

  /**
   * auto generate password register
   * 
   * @param username The username of the user.
   * @param email    The email of the user.
   * @return An AuthResultForm object containing the status and message of the
   *         login attempt.
   */
  public AdminCreateUserResult adminCreateUser(String username, String email);

  /**
   * enable user
   * 
   * @param username The username of the user.
   * @return An AuthResultForm object containing the status and message of the
   *         login attempt.
   */
  public AdminEnableUserResult enableUser(String username);

  /**
   * dis user
   * 
   * @param username The username of the user.
   * @return An AuthResultForm object containing the status and message of the
   *         login attempt.
   */
  public AdminDisableUserResult disableUser(String username);
}
