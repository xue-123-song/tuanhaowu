package com.example.tuanhaowu.Config;

import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.Filter.TokenLoginFilter;
import com.example.tuanhaowu.Filter.TokenVerifyFilter;
import com.example.tuanhaowu.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userService;


    @Bean
    public TokenLoginFilter loginFilter() throws Exception {
        TokenLoginFilter loginFilter = new TokenLoginFilter();
        loginFilter.setUsernameParameter("account");//设置名称loginFilter.setPasswordParameter("password");
        loginFilter.setFilterProcessesUrl("/loginCheck");//设置请求处理的路径，这个会取代userController里的方法
       /*2.注入authenticationManager，这个bean是在configure里用@Bean注入的*/
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        /**
         * 这里是验证通过后用用户的信息生成相应的token
         */
        loginFilter.setAuthenticationSuccessHandler(((request, response, authentication) ->
        {
            Map<String,String> map = new HashMap<>();
            User user = (User) authentication.getPrincipal();
            user.setPassword("");

            map.put("account",authentication.getName());
            map.put("userId",user.getUserid());
//            System.out.println("authentication = "+JSON.toJSONString(authentication));
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> list = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                list.add(authority.getAuthority());
            }
            map.put("roles", JSON.toJSONString(list));
            String token = JWTUtils.getToken(map);
            response.addHeader("Authorization","Bearer"+token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            try {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpStatus.OK.value());
//                System.out.println("user here is"+JSON.toJSONString(user));
                response.getWriter().println(JSON.toJSONString(user));
            }catch (Exception outEx){
                outEx.printStackTrace();
            }
        }));
        return loginFilter;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //指定认证对象的来源
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
    //SpringSecurity配置信息
    public void configure(HttpSecurity http) throws Exception {        /**
                                    9/8更新 by Xu；所有后端不需要登录就能访问的api统一增加一个tourist前缀*/
        http.cors().
                and()
                .csrf().disable().
                authorizeRequests()
                .antMatchers( "/picture/*","/tourist/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new TokenVerifyFilter(super.authenticationManager())) /**这里是每次请求都会验证token的过滤器*/
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);  /**这是每次登录生成token的过滤器*/
    }
}

//@Configuration
//@EnableWebSecurity
//@EnableAutoConfiguration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    /*注入service*/
//    @Autowired
//    UserDetailsService userService;
//    /**
//     * step4：这里是自定义的AuthenticationManager，用于注入到LoginFilter里面进行对于前端请求的认证
//     * 这里面设置了数据来源，可以转UserDetailsService查看step5
//     * */
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//      auth.userDetailsService(userService);
//    }
//
//
//    /**
//     *step3：在Security中注入这个LoginFilter，然后对于这个filter进行一系列的工具注入
//     */
//    @Bean
//    public LoginFilter loginFilter() throws Exception {
//        LoginFilter loginFilter = new LoginFilter();
//        /*1.重新命名前端传过来的数据名称，使之于username和password对应*/
//        loginFilter.setUsernameParameter("account");//设置名称
//        loginFilter.setPasswordParameter("password");
//        loginFilter.setFilterProcessesUrl("/loginCheck");//设置请求处理的路径，这个会取代userController里的方法
//        /*2.注入authenticationManager，这个bean是在configure里用@Bean注入的，这里可以转step4*/
//        loginFilter.setAuthenticationManager(authenticationManagerBean());
//        /*3.设置认证成功之后的处理*/
//        loginFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
//            response.setStatus(HttpStatus.OK.value());
//            response.setContentType("application/json;charset=UTF-8");
//            com.example.tuanhaowu.Entity.User user = (com.example.tuanhaowu.Entity.User) authentication.getPrincipal();
//            user.setPassword("");
//            response.getWriter().println(JSON.toJSONString(user));
//        }));//认证成功处理
//        /*4.设置失败之后的处理，这里面写的是lambda表达式*/
//        loginFilter.setAuthenticationFailureHandler(((request, response, exception) ->
//        {
//            Map<String,Object> result = new HashMap<String,Object>();
//            result.put("msg","登陆失败"+exception.getMessage());
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.setContentType("application/json;charset=UTF-8");
//            String s = new ObjectMapper().writeValueAsString(result);
//            response.getWriter().println(s);//将结果返回
//        }));//认证失败处理
//        return loginFilter;
//    }
//
//    /**
//     * 这个configure里注入的authenticationManager，在LoginFilter里设置进去
//     * */
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    /**
//     * 这个加密器是自动注入使用的，在进行验证的时候会自动进行加密验证
//     * */
//    @Bean
//    public PasswordEncoder passwordEncoder()
//    {
//        return  new BCryptPasswordEncoder();
//    }
//
//    /**
//     *这是个配置类，其中配置了如何对于请求进行认证，具体的意思可以看下面链式表达式中的意思
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .antMatchers("/register").permitAll()
//                .anyRequest().authenticated()//1.对所有的请求开启认证
//                .and()
//                .formLogin()//2.设置认证的方式是表单认证
//                .and()
//                .exceptionHandling()     //3.设置未认证的异常处理，否则会重定向到一个默认的登录页面，现在是返回一个”尚未认证“
//                .authenticationEntryPoint(((request, response, authException) -> {
//                    response.setContentType("application/json;charset=UTF-8");
//                    response.getWriter().println("尚未认证！");
//                }))
//                .and()
//                .logout()//设置退出的处理
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(((request, response, authentication) -> {
//                    SecurityContextHolder.clearContext();
//                    Map<String, Object> result = new HashMap<String, Object>();
//                    result.put("msg", "注销成功");
//                    result.put("用户信息", (authentication.getPrincipal()));
//                    response.setStatus(HttpStatus.OK.value());
//                    response.setContentType("application/json;charset=UTF-8");
//                    String s = new ObjectMapper().writeValueAsString(result);
//                    response.getWriter().println(s);
//                }))
//                .and()
//                .cors()
//                .and()
//                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());//允许前端获取这个令牌，将令牌保存到cookie中
//        /**
//         * 注意！！！！！！！此时已经开启了csrf防御，所以登录的时候需要先登录一次，获取到XSRF-TOKEN，此时登录时失败的
//         * 然后将这个获取到的Token，加入到请求的header里面，用key，value的形式传递参数到后端请求key = X-XSRF-TOKEN,value就是这个token的值
//         * 然后第二次请求登录就能认证成功了
//         */
//
//
//        /**
//         * step1: 前端发起认证请求，请求中携带用户名和密码，请求首先会被UsernamePasswordAuthenticationFilter拦截
//         *        这里我们自定义一个loginFilter，来取代这个UsernamePasswordAuthenticationFilter来对请求进行拦截
//         *        step2转到loginFilter
//         */
//        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);  //添加过滤器,替换掉第二个参数的过滤器
//    }
//
//}


