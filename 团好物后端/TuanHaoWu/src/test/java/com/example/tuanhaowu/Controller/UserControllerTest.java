package com.example.tuanhaowu.Controller;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvcTest
 * 概述
 * 对模块进行集成测试时，希望能够通过输入URL对Controller进行测试，
 * 如果通过启动服务器，建立http client进行测试，这样会使得测试变得很麻烦，
 * 比如，启动速度慢，测试验证不方便，依赖网络环境等，这样会导致测试无法进行，
 * 为了可以对Controller进行测试，可以通过引入MockMVC进行解决。
 *
 * 简介
 * MockMvc实现了对Http请求的模拟，能够直接使用网络的形式，转换到Controller的调用，
 * 这样可以使得测试速度快、不依赖网络环境，而且提供了一套验证的工具，这样可以使得请求的验证统一而且很方便。
 */

/**
 * UserController的MockMvc测试
 * by Xu
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {


    @Autowired
    MockMvc mockMvc;
    private String token;

    /**
     * 传参类型-------------->传json类型的数据进行测试
     * 验证登录功能
     * by Xu
     * */
    @Test
    public void check() throws Exception {

        /**
         * 正确的登录测试
         * */
        String json1 = "{\"account\":\"XunHuanLi&myCidNum\",\"password\":\"123456\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/loginCheck").content(json1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.account").value("XunHuanLi"))
                .andDo(MockMvcResultHandlers.print());

        /**
         * 用户名或密码错误测试
         * */

        String json2 = "{\"account\":\"XunHuanLi&myCidNum\",\"password\":\"12345\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/loginCheck").content(json2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     *示例：如何在传参的时候加header
     * by Xu
     */
    @Test
    void queryUserInfo() throws Exception {
        this.token = "BearereyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IltdIiwiZXhwIjoxNjYzNDA2NTg1LCJ1c2VySWQiOiI5OTkzNTUxMjE3MjI3MjAyNTYiLCJhY2NvdW50IjoiWHVuSHVhbkxpIn0.x3QhKdpWZqNWT0Gt-10RtSOahmMFBCYRKup-UB3smws";

        /**
         * 能成功获取自己的信息
         * */
        this.mockMvc.perform(MockMvcRequestBuilders.get("/queryUserInfo/999355121722720256")
                .header("Authorization",token)
               // .param("userId","999355121722720256")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.account").value("XunHuanLi"))
                .andDo(MockMvcResultHandlers.print());
        /**
         * 禁止获取它人的信息
        * */
       this.mockMvc.perform(MockMvcRequestBuilders.get("/queryUserInfo/999355121722720257")
                        .header("Authorization",token)
                        // .param("userId","999355121722720256")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    /**
     * 因为在registerInfo的最后多加了个逗号导致测试不通过，需要注意
     * 至此，MockMvc的所有测试基本都能够进行了
     * by Xu
     * */
    @Test
    void register() throws Exception {
        String registerInfo = "{\"name\":\"周111111翔\",\"account\":\"zy222222x\",\"password\":\"123456\",\"picture\":\"www.Zhou.com\",\"tel\":\"18290207267\",\"address\":\"东川路800号\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.get("/tourist/register").content(registerInfo)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
