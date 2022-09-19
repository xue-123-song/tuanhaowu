package com.example.tuanhaowu.Filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tuanhaowu.util.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类是每次请求校验Token是否合法的Filter
 */
public class TokenVerifyFilter  extends BasicAuthenticationFilter {


    public TokenVerifyFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");
        System.out.println("url = "+request.getRequestURI());

        System.out.println("Authorization ===>"+header);
        String requestURI = request.getRequestURI();
        if ( requestURI.contains("/picture/") || requestURI.contains("/tourist/"))
        {
            System.out.println("不需要登录即可访问的api");
            chain.doFilter(request,response);
            return;
        }
        if ((header == null || !header.startsWith("Bearer"))) {
            //如果携带错误的token，则给用户提示请登录！
            System.out.println("进入null的验证分支");
            chain.doFilter(request, response);
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter out = response.getWriter();
            Map resultMap = new HashMap();
            resultMap.put("code", HttpServletResponse.SC_FORBIDDEN);
            resultMap.put("msg", "请登录！");
           response.getWriter().println(resultMap);
        } else {
            //如果携带了正确格式的token要先得到token
            String token = header.replace("Bearer", "");
            //验证tken是否正确
            System.out.println("token ="+token);
            DecodedJWT verify = JWTUtils.verify(token);
            String userName = verify.getClaim("account").asString();
            String userId = verify.getClaim("userId").asString();
            System.out.println("userId = "+userId);
            System.out.println("username = "+userName);
            String roleJSON = verify.getClaim("roles").asString();
            System.out.println("roleJSON = " + roleJSON);
            List<String> roleArray = JSON.parseArray(roleJSON,String.class);
            List<SimpleGrantedAuthority> list = new ArrayList<>();
            for (String s : roleArray) {
                list.add(new SimpleGrantedAuthority(s));
            }
            UsernamePasswordAuthenticationToken authResult =
                    new UsernamePasswordAuthenticationToken(userId, null, list);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            chain.doFilter(request, response);
        }
    }
}
