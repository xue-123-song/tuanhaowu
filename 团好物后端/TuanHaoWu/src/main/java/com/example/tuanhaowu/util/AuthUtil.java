package com.example.tuanhaowu.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static String getAuthUserId() {
        System.out.println("context"+SecurityContextHolder.getContext());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }
    /*
    @RequestMapping(value = "/SubscribeTest")
    public void SubscribeTest(@RequestBody SubScribeRequest subScribeTest) throws SchedulerException {
        pushService.SubScribeTest(subScribeTest);
    }
*/
}
