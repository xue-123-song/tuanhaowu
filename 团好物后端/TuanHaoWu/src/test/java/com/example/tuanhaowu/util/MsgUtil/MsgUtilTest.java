package com.example.tuanhaowu.util.MsgUtil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MsgUtilTest {

    /*测试尝试，需要重写equal函数来进行比较，否则*/
    @Test
    void makeMsg() {
        assertEquals(new Msg(MsgCode.SUCCESS),
                MsgUtil.makeMsg(MsgCode.SUCCESS));
    }
}