package com.example.shortlink.project.config;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SentinelRuleConfig implements InitializingBean{
    @Override
    public void afterPropertiesSet() throws Exception {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule createShortLinkRule = new FlowRule();
        createShortLinkRule.setResource("createShortLink");
        createShortLinkRule.setCount(1000);
        rules.add(createShortLinkRule);
        FlowRuleManager.loadRules(rules);
    }
}
