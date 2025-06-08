package com.example.shortlink.project.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


public class LinkUtil {

    public static final long ONE_WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 获取短链接缓存过期时间戳
     *
     * @param date 日期
     * @return 短链接缓存过期时间戳
     */
    public static long getShortLinkCacheTime(Date date) {
        // 当前时间
        long currentTimeMillis = System.currentTimeMillis();

        // 如果 date 为 null，则默认设置为一周后的时间戳，并随机一个 0 ~ 24 小时的时间
        if (date == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            return calendar.getTimeInMillis() + ThreadLocalRandom.current().nextLong(0, 24 * 60 * 60 * 1000L);
        }

        long dateMillis = date.getTime();

        // 检查 date 是否超过一周
        if (dateMillis - currentTimeMillis > ONE_WEEK_MILLIS) {
            // 如果超过一周，那么将有效时间设置在一周后
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            return calendar.getTimeInMillis() + ThreadLocalRandom.current().nextLong(0, 24 * 60 * 60 * 1000L);
        } else {
            // 如果没有超过一周，则设置为 date 的时间戳
            return dateMillis;
        }
    }

}