package com.example.springboot.feature_caching.services;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * to add values to cache
     *
     * @param key
     * @param value
     * @param expirationInSeconds
     */
    @Override
    public void put(String key, Object value, long expirationInSeconds) {
        redisTemplate.opsForValue().set(key, value, expirationInSeconds, TimeUnit.SECONDS);
    }

    /**
     * to get data from cache, in this case it is report type and transaction list
     *
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return clazz.cast(value);
    }

    /**
     * remove one value in cache based on key value
     *
     * @param key
     */
    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    /** remove all values in cache */
    @Override
    public void evictReportCache() {
        evict("report_WEEKLY");
        evict("report_MONTHLY");
        evict("report_YEARLY");
    }
}
