package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

/** Configuration class for setting up Redis and RestTemplate beans. */
@Configuration
public class RedisConfig {

    /**
     * Configures a RedisTemplate for interacting with Redis.
     *
     * <p>This template: - Uses StringRedisSerializer for key and hash key serialization. - Uses
     * GenericJackson2JsonRedisSerializer for value and hash value serialization. - Sets the Redis
     * connection factory for communication with Redis.
     *
     * @param connectionFactory The factory that creates Redis connections.
     * @return A configured RedisTemplate for handling Redis operations.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Creates a RestTemplate bean for making HTTP requests.
     *
     * @return A new instance of RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
