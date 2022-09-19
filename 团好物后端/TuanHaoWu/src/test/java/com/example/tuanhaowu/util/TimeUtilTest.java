package com.example.tuanhaowu.util;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {

    @Test
    public void testTimeTrim() throws Exception {

        /*
        * 测试满足yyyy-MM-dd HH:mm:ss格式的前端和后端时间是否能转化成时间戳
        * */
        assertEquals(new Timestamp(122,6,4,18,12,57,0),TimeUtil.timeTrim("2022-07-04 18:12:57"));
        assertEquals(new Timestamp(122,5,28,3,4,20,0),TimeUtil.timeTrim("2022-06-28T03:04:20.000Z"));
        /*
        * 测试异常输入
        * */
        assertEquals(new Timestamp(0),TimeUtil.timeTrim(" 2022-06-28T03:04:20.000Z"));
        assertEquals(new Timestamp(0),TimeUtil.timeTrim("asajkasjcjl"));
        assertEquals(new Timestamp(0),TimeUtil.timeTrim("16114151616156"));
        assertEquals(new Timestamp(0),TimeUtil.timeTrim("1611-as-jb-la16156"));
    }
    @Test
    public void testGetZero() throws ParseException {
        Timestamp zero = TimeUtil.getZero();
        Timestamp now = TimeUtil.getNow();
        int flag = zero.compareTo(now);
        assertEquals(-1,flag);
        System.out.println("零点是："+TimeUtil.getZero());
    }

    @Test
    void getNow() {
       System.out.println("现在的时间是"+TimeUtil.getNow());
    }

    @Test
    void parseTimeStamp() {
        /*"2022-07-04 18:12:57"*/
       Timestamp t =  new Timestamp(122,6,4,18,12,57,0);
        int [] res= new int[6];
        res = TimeUtil.parseTimeStamp(t);
        assertEquals(2022,res[0]);
        assertEquals(7,res[1]);
        assertEquals(4,res[2]);
        assertEquals(17,res[3]);
        assertEquals(12,res[4]);
        assertEquals(57,res[5]);
        for (int i :res)
        System.out.println(i);
    }
}