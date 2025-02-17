package com.example.springboot.feature_caching.services;

import com.example.springboot.feature_caching.services.CacheService;
import com.example.springboot.feature_report.models.Report;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
//    private final CacheService cacheService;

    public CacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
//        this.cacheService = cacheService;
    }

    @Override
    public void put(String key, Object value, long expirationInSeconds) {
        redisTemplate.opsForValue().set(key, value, expirationInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return clazz.cast(value);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void evictReportCache() {
        evict("report_WEEKLY");
        evict("report_MONTHLY");
        evict("report_YEARLY");
    }



}
