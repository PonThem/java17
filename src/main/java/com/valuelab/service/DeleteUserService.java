package com.valuelab.service;

import com.valuelab.form.DeleteUserForm;

public interface DeleteUserService {

        /**
         * Deletes a user from DB and Cognito
         * 
         * @param userId String
         */
        public void delete(DeleteUserForm form);
}
