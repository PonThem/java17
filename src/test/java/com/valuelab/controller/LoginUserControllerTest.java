package com.valuelab.controller;

import com.valuelab.dto.GetUserInfoDto;
import com.valuelab.service.GetUserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GetUserInfoService getUserInfoService;

    @InjectMocks
    private LoginUserController loginUserController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginUserController).build();
    }

    @Test
    public void testGetUserInfo() throws Exception {
        // Prepare mock response from service
        GetUserInfoDto getUserInfoDto = new GetUserInfoDto();
        when(getUserInfoService.getUserInfo()).thenReturn(getUserInfoDto);

        // Expected JSON response
        String expectedJsonResponse = "{\"statusCode\":200,\"data\":{}}";

        // Perform the test and verify the result
        mockMvc.perform(get("/api/user/getuserinfo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJsonResponse));
    }
}
