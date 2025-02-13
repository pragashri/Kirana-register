package com.example.springboot.feature_caching.services;

public interface CacheService {
    <T> void put(String key, T value);
    <T> T get(String key, Class<T> type);
    void evict(String key);
    void evictAll();
}
