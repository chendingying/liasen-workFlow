package com.liansen.identity.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by CDZ on 2018/11/8.
 */
@Configuration
public class JedisConfig {
    @Value("${spring.redis.maxTotal}")
    public Integer maxTotal;

    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public Integer port;

    @Value("${spring.redis.password}")
    public String password;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool() {
        JedisPool jedisPool = new JedisPool(jedisPoolConfig(), host, port, 30);
        return jedisPool;
    }

}
