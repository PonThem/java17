package com.valuelab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valuelab.dto.GetUserInfoDto;
import com.valuelab.dto.ResponseBodyDto;
import com.valuelab.common.util.CreateResponseFactory;
import com.valuelab.service.GetUserInfoService;

@RestController
@RequestMapping("/api/user")
public class LoginUserController {

    @Autowired
    GetUserInfoService getUserInfoService;

    /**
     * ログインユーザー情報取得
     * 
     * @return uuid 識別ID
     * @return userId ユーザーID
     * @return userName 氏名
     * @return mailAddress メールアドレス
     * @return userType ユーザー種別(0：一般、1：管理者、2：システム管理者)
     */
    @GetMapping("/getuserinfo")
    public ResponseBodyDto<GetUserInfoDto> getUserInfo() {
        GetUserInfoDto response = getUserInfoService.getUserInfo();
        return CreateResponseFactory.normal(response);
    };

}
