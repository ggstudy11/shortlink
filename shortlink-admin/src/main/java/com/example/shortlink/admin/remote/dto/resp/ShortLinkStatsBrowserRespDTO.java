package com.example.shortlink.admin.remote.dto.resp;

import lombok.Data;

@Data
public class ShortLinkStatsBrowserRespDTO {

    private Integer cnt;

    private String browser;

    private Double ratio;
}
