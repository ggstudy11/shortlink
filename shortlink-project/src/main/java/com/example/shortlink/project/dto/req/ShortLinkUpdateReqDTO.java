package com.example.shortlink.project.dto.req;

import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkUpdateReqDTO {

    private String originUrl;

    private String originGid;

    private String fullShortUrl;

    private String gid;

    private Integer validDateType;

    private Date validDate;

    private String describe;
}
