package com.example.shortlink.project.utils;

import com.example.shortlink.project.common.convention.exception.ServiceException;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.common.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeafUtil {

    private final IDGen leafSegmentService;

    public Long nextId() {
        Result result = leafSegmentService.get("short-link-create");
        if (result.getStatus() == Status.SUCCESS) {
            log.info("美团Leaf-segment ID生成器:{}", result);
            return result.getId();
        } else {
            throw new ServiceException("业务异常");
        }
    }
}
