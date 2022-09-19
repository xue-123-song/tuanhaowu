//package com.example.tuanhaowu.Filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 自定义的前后端分离的登录过滤器
// */
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    /**
//     *step2：loginFilter继承了UsernamePasswordAuthenticationFilter，我们对其中的attemptAuthentication方法进行重写
//     * 该方法的作用就是尝试将请求中的username和password封装成一个usernamePasswordAuthenticationToken
//     * 然后及那个这个对象交给AuthenticationManager进行认证
//     */
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        /*1.判断是不是post*/
//        if(!request.getMethod().equals("POST")){
//            throw  new AuthenticationServiceException("Method not supported!");
//        }
//        /* 2.判断是不是json格式的*/
//        if(request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
//        {
//            try
//            {  /*3.从json获取用户名和密码*/
//                Map<String,String> userInfo= new ObjectMapper().readValue(request.getInputStream(),Map.class);
//                String username = userInfo.get(getUsernameParameter());
//                String password = userInfo.get(getPasswordParameter());
//                /*4.进行封装*/
//                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,password);
//                setDetails(request,authRequest);
//                /*5.获取AuthenticationManager，调用authenticate()方法进行认证*/
//                return this.getAuthenticationManager().authenticate(authRequest); //返回这个Token
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return super.attemptAuthentication(request, response);
//    }
//}
