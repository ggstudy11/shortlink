package com.example.shortlink.project.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {

    // 声明持久化队列
    @Bean
    public Queue statsQueue() {
        return new Queue("stats.queue", true); // durable = true
    }

    // 声明持久化交换机
    @Bean
    public DirectExchange statsExchange() {
        return new DirectExchange("stats.exchange", true, false);
    }

    // 绑定队列到交换机
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(statsQueue())
                .to(statsExchange())
                .with("stats.routing.key");
    }

}
