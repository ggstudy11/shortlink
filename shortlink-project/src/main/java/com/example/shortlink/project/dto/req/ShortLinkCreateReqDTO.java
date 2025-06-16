package com.example.shortlink.project.dto.req;

import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkCreateReqDTO {

    private String originUrl;

    private String gid;

    private Integer createdType;

    private Integer validDateType;

    private Date validDate;

    private String describe;

    private String favicon;
}
