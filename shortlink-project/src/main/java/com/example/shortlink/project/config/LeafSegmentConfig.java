package com.example.shortlink.project.config;

import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import com.sankuai.inf.leaf.segment.dao.impl.IDAllocDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LeafSegmentConfig {

    @Bean
    public IDGen leafSegmentService(DataSource dataSource) {
        // 创建号段模式生成器
        SegmentIDGenImpl idGen = new SegmentIDGenImpl();

        // 设置数据库访问对象
        IDAllocDaoImpl dao = new IDAllocDaoImpl(dataSource);
        idGen.setDao(dao);

        // 初始化
        idGen.init();

        return idGen;
    }
}