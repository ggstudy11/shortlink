package com.example.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shortlink.project.dao.entity.ShortLinkAccessLogsDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShortLinkAccessReqDTO extends Page<ShortLinkAccessLogsDO> {

    private String fullShortUrl;

    private String startDate;

    private String endDate;
}
