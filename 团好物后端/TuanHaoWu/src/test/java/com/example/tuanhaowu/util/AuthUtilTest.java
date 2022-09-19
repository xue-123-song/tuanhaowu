package com.example.tuanhaowu.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class AuthUtilTest {

    @Test
    void getAuthUserId() {
        System.out.println(SecurityContextHolder.getContext());
    }
}