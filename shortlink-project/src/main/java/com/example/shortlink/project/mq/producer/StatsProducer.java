package com.example.shortlink.project.mq.producer;

import com.alibaba.fastjson.JSON;
import com.example.shortlink.project.mq.message.StatsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatsProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(StatsMessage payload) {
        /* send message with unique id*/
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(
                "stats.exchange",
                "stats.routing.key",
                JSON.toJSONString(payload),
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
                    return message;
                },
                correlationData
        );
    }
}
