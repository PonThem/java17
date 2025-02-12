package com.valuelab.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

import com.valuelab.dto.AddUserDto;
import com.valuelab.dto.ResponseBodyDto;
import com.valuelab.dto.SearchUserDtos;
import com.valuelab.dto.UpdateUserDto;
import com.valuelab.form.AddUserForm;
import com.valuelab.form.DeleteUserForm;
import com.valuelab.form.SearchUserForm;
import com.valuelab.form.UpdateUserForm;
import com.valuelab.service.AddUserService;
import com.valuelab.service.DeleteUserService;
import com.valuelab.service.SearchUserService;
import com.valuelab.service.UpdateUserService;

public class UserControllerTest {

        @InjectMocks
        private UserController userController;

        @Mock
        private SearchUserService searchUserService;

        @Mock
        private AddUserService addUserService;

        @Mock
        private UpdateUserService updateUserService;

        @Mock
        private DeleteUserService deleteUserService;

        @Mock
        private BindingResult bindingResult;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testSearchUser() {
                SearchUserForm searchUserForm = new SearchUserForm();
                SearchUserDtos expectedResponse = new SearchUserDtos();

                when(searchUserService.searchUser(any(), any(), any(), any())).thenReturn(expectedResponse);

                ResponseBodyDto<SearchUserDtos> response = userController.searchUser(searchUserForm, bindingResult);

                verify(searchUserService).searchUser(any(), any(), any(), any());
                assertEquals(expectedResponse, response.getData());
        }

        @Test
        public void testAddUser() {
                AddUserForm addUserForm = new AddUserForm();
                AddUserDto expectedResponse = new AddUserDto();

                when(addUserService.register(any(AddUserForm.class))).thenReturn(expectedResponse);

                ResponseBodyDto<AddUserDto> response = userController.addUser(addUserForm, bindingResult);

                verify(addUserService).register(addUserForm);
                assertEquals(expectedResponse, response.getData());
        }

        @Test
        public void testUpdateUser() {
                UpdateUserForm updateUserForm = new UpdateUserForm();
                UpdateUserDto expectedResponse = new UpdateUserDto();

                when(updateUserService.update(any(UpdateUserForm.class))).thenReturn(expectedResponse);

                ResponseBodyDto<UpdateUserDto> response = userController.updateUser(updateUserForm, bindingResult);

                verify(updateUserService).update(updateUserForm);
                assertEquals(expectedResponse, response.getData());
        }

        @Test
        public void testDeleteUser() {
                DeleteUserForm deleteUserForm = new DeleteUserForm();

                ResponseBodyDto<Void> response = userController.deleteUser(deleteUserForm, bindingResult);

                verify(deleteUserService).delete(deleteUserForm);
                assertEquals(null, response.getData());
        }
}
