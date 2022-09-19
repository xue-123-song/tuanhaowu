package com.example.tuanhaowu.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnowFlakeUtilTest {

    @Test
    void nextId() {

        String userId1 = SnowFlakeUtil.nextId();
        String userId2 = SnowFlakeUtil.nextId();
        assertNotEquals(userId1,userId2);
    }
}