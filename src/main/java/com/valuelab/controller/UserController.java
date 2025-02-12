package com.valuelab.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valuelab.dto.AddUserDto;
import com.valuelab.dto.ResponseBodyDto;
import com.valuelab.dto.SearchUserDtos;
import com.valuelab.dto.UpdateUserDto;
import com.valuelab.form.AddUserForm;
import com.valuelab.form.DeleteUserForm;
import com.valuelab.form.SearchUserForm;
import com.valuelab.form.UpdateUserForm;
import com.valuelab.service.AddUserService;
import com.valuelab.common.util.CreateResponseFactory;
import com.valuelab.service.DeleteUserService;
import com.valuelab.service.SearchUserService;
import com.valuelab.service.UpdateUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/setting/user")
public class UserController {

    @Autowired
    SearchUserService searchUserService;

    @Autowired
    AddUserService addUserService;

    @Autowired
    UpdateUserService updateUserService;

    @Autowired
    DeleteUserService deleteUserService;

    /**
     * ユーザー検索
     * 
     * @param userId      ユーザーID
     * @param userName    氏名
     * @param mailAddress メールアドレス
     * @param userType    ユーザー種別(0：一般、1：管理者)
     * @return userList
     */
    @GetMapping(value = "/searchuser", params = { "userId", "userName", "mailAddress", "userType" })
    public ResponseBodyDto<SearchUserDtos> searchUser(@ParameterObject @ModelAttribute SearchUserForm searchUserForm,
            BindingResult bindingResult) {
        SearchUserDtos response = searchUserService.searchUser(
                searchUserForm.getUserId(),
                searchUserForm.getUserName(),
                searchUserForm.getMailAddress(),
                searchUserForm.getUserType());
        return CreateResponseFactory.normal(response);

    }

    /**
     * ユーザー追加
     * 
     * @param userId      ユーザーID
     * @param userName    氏名
     * @param mailAddress メールアドレス
     * @param userType    ユーザー種別(0：一般、1：管理者)
     * @return uuid 識別ID
     * @return updatedDatetime 更新日時
     */
    @PostMapping("/adduser")
    public ResponseBodyDto<AddUserDto> addUser(@Valid @RequestBody AddUserForm addUserForm,
            BindingResult bindingResult) {
        AddUserDto addUserDto = addUserService.register(addUserForm);
        return CreateResponseFactory.normal(addUserDto);

    }

    /**
     * ユーザー編集
     * 
     * @param uuid            識別ID
     * @param userId          ユーザーID
     * @param userName        氏名
     * @param mailAddress     メールアドレス
     * @param userType        ユーザー種別(0：一般、1：管理者)
     * @param updatedDatetime 元データの更新日時
     * @return uuid 識別ID
     * @return updatedDatetime 更新日時
     */
    @PostMapping("/updateuser")
    public ResponseBodyDto<UpdateUserDto> updateUser(@Valid @RequestBody UpdateUserForm updateUserForm,
            BindingResult bindingResult) {
        UpdateUserDto updateUserDto = updateUserService.update(updateUserForm);
        return CreateResponseFactory.normal(updateUserDto);
    }

    /**
     * ユーザー削除
     * 
     * @param uuid            識別ID
     * @param updatedDatetime 元データの更新日時
     */
    @PostMapping("/deleteuser")
    public ResponseBodyDto<Void> deleteUser(@Valid @RequestBody DeleteUserForm deleteUserForm,
            BindingResult bindingResult) {
        deleteUserService.delete(deleteUserForm);
        return CreateResponseFactory.normal(null);
    }

}
