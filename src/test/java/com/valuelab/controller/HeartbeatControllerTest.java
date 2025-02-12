package com.valuelab.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HeartbeatControllerTest {

        private MockMvc mockMvc;

        @InjectMocks
        private HeartbeatController heartbeatController;

        @BeforeEach
        public void setup() {
                MockitoAnnotations.openMocks(this);
                mockMvc = MockMvcBuilders.standaloneSetup(heartbeatController).build();
        }

        @Test
        public void testHeartbeat() throws Exception {
                mockMvc.perform(get("/heartbeat"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("OK"));
        }
}
