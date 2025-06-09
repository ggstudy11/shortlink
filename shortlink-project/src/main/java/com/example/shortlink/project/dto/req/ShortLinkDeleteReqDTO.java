package com.example.shortlink.project.dto.req;

import lombok.Data;

@Data
public class ShortLinkDeleteReqDTO {
    private String gid;
    private String fullShortUrl;
}
