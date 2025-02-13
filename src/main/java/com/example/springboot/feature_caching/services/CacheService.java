package com.example.springboot.feature_caching.services;

public interface CacheService {
    void put(String key, Object value, long expirationInSeconds);
    <T> T get(String key, Class<T> clazz);
    void evict(String key);
    void evictReportCache();
}
