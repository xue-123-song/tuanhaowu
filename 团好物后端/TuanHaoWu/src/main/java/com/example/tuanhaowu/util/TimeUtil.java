package com.example.tuanhaowu.util;

import org.apache.commons.lang.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/*转化时间的工具类*/
public class TimeUtil {
    /**
     * 将前端时间格式转化为后端的时间戳
     * @param foreTime
     * @return 时间戳
     * by Xu
     */
    public static Timestamp timeTrim(String foreTime)
    {
            String st = foreTime.replace(".000Z", "");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime;
            try {
               localDateTime = LocalDateTime.parse(st.replaceAll("T", " "), df);
            }
            catch ( DateTimeParseException err)
            {
                return new Timestamp(0);
            }

            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp;
    }

    /**
     * 获取当天的零点时间
     * @return 比如：2022-08-14 00:00:00.0
     * @throws ParseException
     * by Xu
     */
    public static Timestamp getZero() throws ParseException
    {
        return TimeUtil.timeTrim(DateFormatUtils.format(new Date(),"yyyy-MM-dd 00:00:00"));
    }

    /**
     * 获取当前的时间戳
     * @return 比如：2022-08-14 11:26:43.059
     * by Xu
     */
    public static Timestamp getNow()
    {
        return new Timestamp(System.currentTimeMillis());
    }

    public static int[] parseTimeStamp(Timestamp t)
    {
        int [] date = new int[6];
        date[0] = t.getYear()+1900;
        date[1] = t.getMonth()+1;
        date[2] = t.getDate();
        date[3] = t.getHours();
        date[4] = t.getMinutes();
        date[5] = t.getSeconds();
        //定时任务提前一小时开始
        if(date[3] == 0)
        {
            date[2]--;
            date[3] = 23;
        }
        else
            date[3]--;
        return date;
    }
}
