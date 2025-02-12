package com.valuelab.service;

import com.valuelab.form.AddUserForm;
import com.valuelab.dto.AddUserDto;

public interface AddUserService {

    /**
     * Registers a new user into DB and Cognito
     * 
     * @param addUserForm AddUserForm
     * @return AddUserDto
     */
    public AddUserDto register(AddUserForm addUserForm);
}
