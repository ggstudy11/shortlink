package com.example.shortlink.project.dto.resp;

import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkPageRespDTO {

    private Long id;

    private String domain;

    private String shortUri;

    private String fullShortUrl;

    private String originUrl;

    private Integer clickNum;

    private String gid;

    private Integer validDateType;

    private Date validDate;

    private String describe;

    private String favicon;
}
