package com.example.shortlink.project;

import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class BaiduApiTest {

    public static String URL = "https://api.map.baidu.com/location/ip?";

    @Test
    public void test() {
        Map<String, Object> params = new HashMap<>();
        params.put("ip", "114.247.244.10");
        params.put("ak", "Rio8YC3Nr0EcoUMQW3MQIhGHjttGvPhx");
        String jsonStr = HttpUtil.get(URL, params);
        System.out.println(jsonStr);
    }
}
