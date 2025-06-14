package com.example.shortlink.project.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.shortlink.project.dao.entity.ShortLinkLocaleStatsDO;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class LinkUtil {

    public static final long ONE_WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000L;

    public static final String BAIDU_URL = "https://api.map.baidu.com/location/ip?";

    public static final String AK = "Rio8YC3Nr0EcoUMQW3MQIhGHjttGvPhx";

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

    public static ShortLinkLocaleStatsDO getLocaleStats(String ip, String fullShortUrl) {
        // 发送HTTP请求
        Map<String, Object> params = new HashMap<>();
        params.put("ip", ip);
        params.put("ak", AK);
        String jsonStr = HttpUtil.get(BAIDU_URL, params);

        // 解析JSON结果
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = jsonObject.getIntValue("status");
        ShortLinkLocaleStatsDO shortLinkLocaleStatsDO;
        if (status == 0) { // 成功
            JSONObject content = jsonObject.getJSONObject("content");
            JSONObject addressDetail = content.getJSONObject("address_detail");

            shortLinkLocaleStatsDO = ShortLinkLocaleStatsDO
                    .builder()
                    .adcode(addressDetail.getString("adcode"))
                    .cnt(1)
                    .date(new Date())
                    .fullShortUrl(fullShortUrl)
                    .province(addressDetail.getString("province"))
                    .city(addressDetail.getString("city"))
                    .build();
        } else { // 失败
            shortLinkLocaleStatsDO = ShortLinkLocaleStatsDO
                .builder()
                .adcode("未知")
                .cnt(1)
                .date(new Date())
                .fullShortUrl(fullShortUrl)
                .province("未知")
                .city("未知")
                .build();
        }

        return shortLinkLocaleStatsDO;
    }
}
