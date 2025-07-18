package com.example.shortlink.admin.remote.dto.req;

import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkUpdateReqDTO {

    private String originUrl;

    private String originGid;

    private String fullShortUrl;

    private String gid;

    private String orginGid;

    private Integer validDateType;

    private Date validDate;

    private String describe;
}
