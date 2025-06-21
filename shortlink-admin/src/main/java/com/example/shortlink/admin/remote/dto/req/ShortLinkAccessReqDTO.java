package com.example.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShortLinkAccessReqDTO extends Page {

    private String fullShortUrl;

    private String startDate;

    private String endDate;
}
