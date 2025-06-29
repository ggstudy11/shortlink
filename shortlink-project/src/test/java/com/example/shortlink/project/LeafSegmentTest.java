package com.example.shortlink.project;

import com.example.shortlink.project.utils.HashUtil;
import com.example.shortlink.project.utils.LeafUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LeafSegmentTest {

    @Autowired
    private LeafUtil leafUtil;

    @Test public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println(HashUtil.convertDecToBase62(leafUtil.nextId()));
        }
    }

}
