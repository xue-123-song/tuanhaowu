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
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;
    private String token;
    @BeforeAll
    void getToken()
    {
        this.token =  "BearereyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IltdIiwiZXhwIjoxNjYzNDA2NTg1LCJ1c2VySWQiOiI5OTkzNTUxMjE3MjI3MjAyNTYiLCJhY2NvdW50IjoiWHVuSHVhbkxpIn0.x3QhKdpWZqNWT0Gt-10RtSOahmMFBCYRKup-UB3smws";
    }
    @Test
    void searchOrderByUserId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/searchOrderByUserId/999355121722720256")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getOrdersForGroupLeader() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/getOrdersForGroupLeader/999355121722720256")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void joinGroup() throws Exception {
        String info =" {\n" +
                "        \"groupId\":1055,\n" +
                "        \"userId\":\"999355121722720256\",\n" +
                "         \"tel\":\"18290207267\",\n" +
                "        \"address\":\"东川路800号\",\n" +
                "        \"itemId\":[1125],\n" +
                "        \"num\":[1,2,3,4],\n" +
                "        \"receiverName\":\"花满楼\"\n" +
                "    }";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/joinGroup").content(info)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**周千翔来写测试*/
//    @Test
//    void payOrder() throws Exception {
//        String info = "  {\n" +
//                "        \"orderId\":21,\n" +
//                "        \"userId\":\"999355121722720256\"\n" +
//                "    }";
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/payOrder").content(info)
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .header("Authorization",this.token)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    void cancelOrder() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cancelOrder/21")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deliverOrder() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/deliverOrder/21")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void receiveOrder() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/receiveOrder/21")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void modifyOrderInfo() throws Exception {
        String info1 = "  {\n" +
                "        \"orderId\":3,\n" +
                "        \"tel\":\"17745264387\"\n" +
                "     \n" +
                "    }";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/modifyOrderInfo").content(info1)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        String info2 = "  {\n" +
                "        \"orderId\":23,\n" +
                "        \"tel\":\"17745264387\"\n" +
                "    }";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/modifyOrderInfo").content(info2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void leaderDeleteOrder() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/LeaderDeleteOrder/21")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/LeaderDeleteOrder/3")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void memberDeleteOrder() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/MemberDeleteOrder/21")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/MemberDeleteOrder/3")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getRecommend() throws Exception {
        String info = "{\n" +
                "        \"userId\":\"999355121722720256\"\n" +
                "    }";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/recommend").content(info)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        /*userId = null*/
        String info1 = "{\n" +
                "    }";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/recommend").content(info1)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void secKill() throws Exception {
        String info = " {\n" +
                "    \"groupId\" : \"1\",\n" +
                "    \"userId\" : \"1\",\n" +
                "    \"tel\" : \"11111\",\n" +
                "    \"address\" : \"111\",\n" +
                "    \"itemId\" : \"30\"\n" +
                "    }\n";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/secKill").content(info)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getLatestGroup() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/getLatestGroup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}