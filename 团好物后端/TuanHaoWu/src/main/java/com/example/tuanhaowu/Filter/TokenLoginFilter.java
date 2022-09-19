package com.example.tuanhaowu.Filter;

import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.util.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * 这个类是用来登录的时候生成token的
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * 具体认证的方法
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        User sysUser = null;
        try {
            sysUser = JSON.parseObject(getJson(request), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
            System.out.println("sysUser = "+JSON.toJSONString(sysUser));
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(sysUser.getAccount(), sysUser.getPassword());
            setDetails(request,authRequest);
            /*5.获取AuthenticationManager，调用authenticate()方法进行认证*/
            return this.getAuthenticationManager().authenticate(authRequest); //返回这个Token
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        try {
            response.setContentType("application/json;charset=utf-8");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            Map resultMap = new HashMap();
            resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
            resultMap.put("msg", "用户名或密码错误！");
            out.write(new ObjectMapper().writeValueAsString(resultMap));
            out.flush();
            out.close();
        }catch (Exception outEx){
            outEx.printStackTrace();
        }
    }

    public String getJson(HttpServletRequest request) throws IOException{
        BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            sb.append(inputStr);
        }
        return sb.toString();
    }
    public String getCid(HttpServletRequest request) throws IOException{
        User sysUser = JSON.parseObject(getJson(request), User.class);
        return sysUser.getUserCid();
    }

}

