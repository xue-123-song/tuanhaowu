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
class HistoryControllerTest {
    @Autowired
    MockMvc mockMvc;
    private String token;
    @BeforeAll
    void getToken()
    {
        this.token =  "BearereyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IltdIiwiZXhwIjoxNjYzNDA2NTg1LCJ1c2VySWQiOiI5OTkzNTUxMjE3MjI3MjAyNTYiLCJhY2NvdW50IjoiWHVuSHVhbkxpIn0.x3QhKdpWZqNWT0Gt-10RtSOahmMFBCYRKup-UB3smws";
    }

    @Test
    void addHistory() throws Exception {
        String info = "{" +
                "\"belongUserId\":\"999355121722720256\"," +
                "\"groupId\":1023" +
                "}";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/history/add").content(info)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
/**
 * ！！！！！需要重置history状态
 * */
    @Test
    void deleteOneHistory() throws Exception {
        /*
        * 正常删除
        * */
        this.mockMvc.perform(MockMvcRequestBuilders.get("/history/deleteOne/14")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        /*
        * 异常删除
        * */
        this.mockMvc.perform(MockMvcRequestBuilders.get("/history/deleteOne/50")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("删除历史记录错误"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void queryAllHistory() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/history/QueryAll/999355121722720256")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void clearAllHistory() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/history/clearAll/999355121722720256")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}