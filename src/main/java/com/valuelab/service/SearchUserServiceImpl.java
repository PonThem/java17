package com.valuelab.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valuelab.dto.SearchUserDto;
import com.valuelab.dto.SearchUserDtos;
import com.valuelab.entity.MstUser;
import com.valuelab.repository.MstUsersRepository;

@Service
@Transactional
public class SearchUserServiceImpl implements SearchUserService {

    @Autowired
    MstUsersRepository mstUsersRepository;

    public SearchUserDtos searchUser(String userId, String userName, String maillAddress, String userType) {
        List<SearchUserDto> list = new ArrayList<>();
        List<MstUser> userList = mstUsersRepository.selectUser(userId, userName, maillAddress, userType);
        for (MstUser mstUser : userList) {
            SearchUserDto dto = SearchUserDto.of(mstUser);
            list.add(dto);
        }
        SearchUserDtos dtos = new SearchUserDtos();
        dtos.setUserList(list);
        return dtos;
    }
}
