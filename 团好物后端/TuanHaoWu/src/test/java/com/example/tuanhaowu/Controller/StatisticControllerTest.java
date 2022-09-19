package com.example.tuanhaowu.Controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatisticControllerTest {
    @Autowired
    MockMvc mockMvc;
    private String token;
    @BeforeAll
    void getToken()
    {
        this.token =  "BearereyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IltdIiwiZXhwIjoxNjYzNDA2NTg1LCJ1c2VySWQiOiI5OTkzNTUxMjE3MjI3MjAyNTYiLCJhY2NvdW50IjoiWHVuSHVhbkxpIn0.x3QhKdpWZqNWT0Gt-10RtSOahmMFBCYRKup-UB3smws";
    }
    @Test
    void getItemSellNum() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/statistic/itemSellNum/1023")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getVisitTimes() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/statistic/getVisitTimes/1023")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void todayIncrementVisit() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/statistic/todayIncrementVisit/1023")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}