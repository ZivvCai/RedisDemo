package com.czw.demo.controller;

import com.czw.demo.annotation.RedissonLock;
import com.czw.demo.domain.GoodDTO;
import com.czw.demo.mapper.GoodMapper;
import com.czw.demo.rabbitmq.MessageProducer;
import com.czw.demo.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 商品出售Controller
 *
 * @author caizw
 */
@Slf4j
@RestController
@Component
public class GoodController {

    @Resource
    private GoodMapper goodMapper;
    @Resource
    private MessageProducer messageProducer;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 浏览器请求调用
     * @param goodId 商品编号
     * @return 是否抢到商品
     */
    @RequestMapping(value = "/secKill", method = RequestMethod.GET)
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(lockIndex = 0)
    public String secKill(Integer goodId) {
        GoodDTO goodDTO = goodMapper.selectGood(goodId);
        int goodTotal = goodDTO.getGoodTotal() - 1;
        log.info("商品total:" + goodTotal);
        if (goodTotal == -1) {
            log.info("该商品已售罄");
            return "很遗憾，您未秒杀到商品：" + goodId;
        } else {
            goodMapper.updateGoodTotal(goodId, goodTotal);
            try {
                messageProducer.sendMessage(JsonUtils.getObjectMapper().writeValueAsString(goodDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "恭喜您成功秒杀到商品：" + goodDTO.toString();
        }
    }

    /**
     * Test调用
     * @param goodId 商品编号
     */
    public void secKillTest(Integer goodId) {
        String key = "sec_kill_test" + goodId;
        RLock rLock = redissonClient.getLock(key);
        int leastTime = 10;
        int waitTime = 5;
        try {
            if (rLock.tryLock(waitTime, leastTime, TimeUnit.SECONDS)) {
                log.info("获得锁");
                GoodDTO goodDTO = goodMapper.selectGood(goodId);
                int goodTotal = goodDTO.getGoodTotal();
                log.info("商品total:" + goodTotal);
                if (goodTotal == 0) {
                    log.info("很遗憾，该商品已售罄");
                } else {
                    goodMapper.updateGoodTotal(goodId, goodTotal - 1);
                    try {
                        messageProducer.sendMessage(JsonUtils.getObjectMapper().writeValueAsString(goodDTO));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    log.info("恭喜您成功秒杀到商品：" + goodDTO.toString());
                }
                rLock.unlock();
                log.info("释放锁");
            } else {
                log.info("没有获得锁");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
