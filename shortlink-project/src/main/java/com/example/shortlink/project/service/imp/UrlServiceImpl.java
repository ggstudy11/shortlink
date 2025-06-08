package com.example.shortlink.project.service.imp;

import com.example.shortlink.project.common.convention.exception.ServiceException;
import com.example.shortlink.project.service.UrlService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl implements UrlService {
    @Override
    public String getTitle(String url) {
        try {
            // 连接到URL并获取HTML文档
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")  // 设置User-Agent，避免被网站拒绝
                    .timeout(5000)             // 设置超时时间
                    .get();
            return doc.title();
        } catch (Exception e) {
            throw new ServiceException("网站标题无法获取");
        }
    }
}
