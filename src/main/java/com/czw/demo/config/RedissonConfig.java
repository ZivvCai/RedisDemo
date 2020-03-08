package com.czw.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Redisson配置
 *
 * @author caizw
 */
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        RedissonClient redissonClient = Redisson.create(
                Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));
        return redissonClient;
    }

}
