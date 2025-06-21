package com.example.shortlink.admin.remote.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkAccessRespDTO {

    private String user;

    private String ip;

    private String browser;

    private String os;

    private String network;

    private String locale;

    private String device;

    private String uvType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
