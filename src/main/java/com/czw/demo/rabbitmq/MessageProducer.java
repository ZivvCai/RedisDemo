package com.czw.demo.rabbitmq;

import com.czw.demo.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * RabbitMq消息生产者
 *
 * @author caizw
 */
@Slf4j
@Component
public class MessageProducer implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendMessage(String message) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.ORDER_EXCHANGE, RabbitMqConfig.ORDER_ROUTINGKEY, message, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("回调id:" + correlationData);
        if (ack) {
            log.info("id:" + correlationData + "消息消费成功！");
        } else {
            log.info("id:" + correlationData + "消息消费失败：" + cause);
        }
    }
}
