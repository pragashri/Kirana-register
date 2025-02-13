package com.example.springboot.feature_caching.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> void put(String key, T value) {
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.HOURS); // Cache for 1 hour
        System.out.println("📝 Cache PUT: " + key + " => " + value);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            System.out.println("Cache MISS for key: " + key);
            return null;
        }
        System.out.println("🔍 Cache GET: " + key + " => " + value);
        return type.cast(value);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
        System.out.println("❌ Cache EVICT: " + key);
    }

    @Override
    public void evictAll() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
