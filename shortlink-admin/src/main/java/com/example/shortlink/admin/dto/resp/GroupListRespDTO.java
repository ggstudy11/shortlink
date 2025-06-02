package com.example.shortlink.admin.dto.resp;

import lombok.Data;

@Data
public class GroupListRespDTO {

    private String gid;

    private String name;

    private String username;

    private Integer sortOrder;
}
