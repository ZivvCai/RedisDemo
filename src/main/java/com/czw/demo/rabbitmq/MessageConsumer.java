package com.czw.demo.rabbitmq;

import com.czw.demo.config.RabbitMqConfig;
import com.czw.demo.domain.GoodDTO;
import com.czw.demo.domain.OrderDTO;
import com.czw.demo.mapper.OrderMapper;
import com.czw.demo.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * RabbitMq消费者
 *
 * @author caizw
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMqConfig.ORDER_QUEUE)
public class MessageConsumer {

    @Resource
    private OrderMapper orderMapper;

    @RabbitHandler
    public void consume(String message) {
        log.info("接收到消息：{}", message);
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        try {
            GoodDTO goodDTO = objectMapper.readValue(message, GoodDTO.class);
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderName("order - " + goodDTO.getGoodName());
            orderDTO.setOrderDescribe("order - " + goodDTO.getGoodDescribe());
            orderMapper.insertOrder(orderDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
