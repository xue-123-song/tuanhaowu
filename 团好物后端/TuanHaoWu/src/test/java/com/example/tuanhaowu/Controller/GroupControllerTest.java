package com.example.tuanhaowu.Controller;

import com.alibaba.fastjson.JSON;
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

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupControllerTest {
    @Autowired
    MockMvc mockMvc;
    private String token;

    @BeforeAll
    void getToken()
    {
       this.token =  "BearereyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IltdIiwiZXhwIjoxNjYzNDA2NTg1LCJ1c2VySWQiOiI5OTkzNTUxMjE3MjI3MjAyNTYiLCJhY2NvdW50IjoiWHVuSHVhbkxpIn0.x3QhKdpWZqNWT0Gt-10RtSOahmMFBCYRKup-UB3smws";
    }
    @Test
    void testCreat() throws Exception {

        String info = "{\"groupTitle\":\"图书清仓甩卖\",\"groupDescription\": \"全场图书只需9块9\",\"startTime\":\"2022-09-10T12:00:00.000Z"+
                "\","+
                "\"endTime\":\"2022-09-11T01:25:04.000Z\",\"logistics\":4,"+
                "\"groupLeader\": \"三明治\","+
                "\"items\":[{"+
                "\"itemName\":\"一剪梅\","+
                    "\"itemStock\":8,"+
                    "\"itemPrice\":999,"+
                    "\"itemSeckill\":true,"+
                    "\"skStartTime\":\"2022-07-04T01:13:40.000Z\","+
                    "\"skEndTime\":\"2022-07-04T01:13:50.000Z\","+
                    "\"itemImage\":["+
           "{\"url\":\"blob:https://localhost:3000/ebad4762-f36b-4242-aa1c-525ad10bf8a9\"}"+
               "],"+
            "\"itemDescription\":\"好书"+
        "\"},"+
        "{"+
            "\"itemName\":\"魔兽世界编年\","+
                "\"itemStock\":5,"+
                "\"itemPrice\":7860,"+
                "\"itemSeckill\":false,"+
                "\"itemImage\":["+
            "{\"url\":\"blob:https://localhost:3000/59ee1090-52d4-405e-a57a-f73988dfb491\"}"+
                "],"+
            "\"itemDescription\":\"好书"+
        "\"}"+
        "]"+
    "}";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/createGroup").content(info)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("创建团购成功！"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testSearchGroupByGroupLeader() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/searchGroupByGroupLeader/goL")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..groupTitle").value("图书特卖，只要998，特别便宜啦!谁不买谁是傻*"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 返回值是数组类型的怎么获取其中的某些字段
     * */

    @Test
    void testSearchGroupItem() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/searchGroupItemByGroupID/1055")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void testSearchAllGroup() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/searchAllGroup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 返回值是<key,value>类型的怎么获取key或者value中的某个字段进行结果的正确性验证
     *
     * */

    @Test
    void testChangeGroupInfo() throws Exception {

        /*无异常的测试*/
        String info1 = "{"+
                "\"groupId\":1055,"+
                "\"groupLeader\":\"李寻欢\","+
                "\"groupTitle\":\"我是测试哈哈哈\","+
                "\"groupDescription\":\"relax\","+
                "\"logistics\":2,"+
                "\"startTime\":\"2022-09-11 06:16:16\","+
                "\"endTime\":\"2024-09-13 06:16:16\""+

    "}";
        /*权限异常*/
        this.mockMvc.perform(MockMvcRequestBuilders.get("/changeGroupInfo").content(info1)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.key.status").value(0))
                .andDo(MockMvcResultHandlers.print());

        String info2 = "{"+
                "\"groupId\":1056,"+
                "\"groupLeader\":\"李寻欢\","+
                "\"groupTitle\":\"我是测试哈哈哈\","+
                "\"groupDescription\":\"relax\","+
                "\"logistics\":2,"+
                "\"startTime\":\"2022-09-11 06:16:16\","+
                "\"endTime\":\"2024-09-13 06:16:16\""+

                "}";

        this.mockMvc.perform(MockMvcRequestBuilders.get("/changeGroupInfo").content(info2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        /*信息异常*/
        String info3 = "{"+
                "\"groupId\":1055,"+
                "\"groupLeader\":\"李寻欢\","+
                "\"groupTitle\":\"我是测试哈哈哈\","+
                "\"groupDescription\":\"relax\","+
                "\"logistics\":2,"+
                "\"startTime\":\"2022-09-11 ???\","+
                "\"endTime\":\"2024-09-13 06:16:16\""+

                "}";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/changeGroupInfo").content(info3)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.key.status").value(0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testDeleteGroupByID() throws Exception {
        /**
         * 正常删除
         * */
        this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteGroupByID/1056")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("删除团购成功"))
                .andDo(MockMvcResultHandlers.print());


        /**
         * 删除异常
         * */
        this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteGroupByID/1055")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("当前团购有尚未处理的订单，请先处理订单再删除"))
                .andDo(MockMvcResultHandlers.print());
        /**
        * 无权限删除
        * */
        this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteGroupByID/1023")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("您没有权限删除不属于自己的团购"))
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    void testSearchGroupByGroupId() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/tourist/searchGroupByGroupId/1023")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    /**
     * !!!!!!每次测试需要将李寻欢所属的三个团购状态重置为1
     * */
    @Test
    void cancelGroup() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/cancelGroup/1054")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void queryOrderNum() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/statistic/QueryData/1055")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("查找数据成功"))
                .andDo(MockMvcResultHandlers.print());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/statistic/QueryData/1023")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("您没有权限查询不属于自己的团购"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void fuzzyQueryByGroupLeader() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/tourist/FuzzyQueryByGroupLeader/我是")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void fuzzyQueryByGroupTitle() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/tourist/FuzzyQueryByGroupTitle/图书清仓")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void fuzzyQueryByItemName() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/tourist/FuzzyQueryByItemName/一")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization",this.token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}