package com.valuelab.service;

import com.valuelab.form.UpdateUserForm;
import com.valuelab.dto.UpdateUserDto;

public interface UpdateUserService {

        /**
         * Updates a user in DB and Cognito
         * 
         * @param updateUserForm UpdateUserForm
         * @return UpdateUserDto
         */
        public UpdateUserDto update(UpdateUserForm updateUserForm);
}
