package com.example.springboot.feature_caching.services;

import com.example.springboot.feature_report.models.Report;

import java.time.LocalDateTime;

public interface CacheService {
    void put(String key, Object value, long expirationInSeconds);
    <T> T get(String key, Class<T> clazz);
    void evict(String key);
    void evictReportCache();

}
