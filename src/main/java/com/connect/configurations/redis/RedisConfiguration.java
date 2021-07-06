package com.connect.configurations.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Value("${redis.host-name}")
    private String hostname;

    @Bean
    public LettuceConnectionFactory redisTemplateConfigurationBean(){
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(hostname, 6379)
        );
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(){
        RedisTemplate<Byte[], Byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisTemplateConfigurationBean());
        return template;
    }

}
