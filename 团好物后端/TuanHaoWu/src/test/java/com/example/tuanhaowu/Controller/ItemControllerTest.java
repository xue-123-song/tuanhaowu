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
class ItemControllerTest {
    @Autowired
    MockMvc mockMvc;
    private String token;
    @BeforeAll
    void getToken()
    {
        this.token =  "BearereyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IltdIiwiZXhwIjoxNjYzNDA2NTg1LCJ1c2VySWQiOiI5OTkzNTUxMjE3MjI3MjAyNTYiLCJhY2NvdW50IjoiWHVuSHVhbkxpIn0.x3QhKdpWZqNWT0Gt-10RtSOahmMFBCYRKup-UB3smws";
    }
    @Test
    void addItemToGroup() throws Exception {
        String info1 = "{ \"groupId\":1055, \"itemName\": \"金庸全集\",\"itemStock\": 8,\"itemPrice\": 999,\"itemSeckill\": true, \"skStartTime\": \"2022-07-04T01:13:40.000Z\", \"skEndTime\": \"2022-07-04T01:13:50.000Z\",\n" +
                "        \"itemImage\": [\n" +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8a9\" }," +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8b9\" }," +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8c9\" }" +
                "        ],\n" +
                "        \"itemDescription\": \"好书\"\n" +
                "    }";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/addItemToGroup").content(info1)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        String info = "{ \"groupId\":1055, \"itemName\": \"金庸全集\",\"itemStock\": 8,\"itemPrice\": 999,\"itemSeckill\": true, \"skStartTime\": \"2022-07-04T01:13:40.000Z\", \"skEndTime\": \"2022-07-04T01:13:50.000Z\",\n" +
                "        \"itemImage\": [\n" +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8a9\" }," +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8b9\" }," +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8c9\" }" +
                "        ],\n" +
                "        \"itemDescription\": \"好书\"\n" +
                "    }";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/addItemToGroup").content(info)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void deleteItemByID() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteItemByID/1124")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void modifyItemInfo() throws Exception {
        String info = " {\n" +
                "        \"itemId\":1122,\n" +
                "        \"itemName\": \"金庸全集???\",\n" +
                "        \"itemStock\": 8,\n" +
                "        \"itemPrice\": 899,\n" +
                "        \"itemSeckill\": true,\n" +
                "        \"skStartTime\": \"2022-07-04T01:13:40.000Z\",\n" +
                "        \"skEndTime\": \"2022-07-04T01:13:50.000Z\",\n" +
                "        \"itemImage\": [\n" +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8a8\" },\n" +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8b7\" },\n" +
                "            { \"url\": \"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8c6\" }\n" +
                "        ],\n" +
                "        \"itemDescription\": \"金庸老爷子的一生著作皆在于此！！\"\n" +
                "    }";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/modifyItemInfo").content(info)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}