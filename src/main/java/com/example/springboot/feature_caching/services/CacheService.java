package com.example.springboot.feature_caching.services;

public interface CacheService {

    /**
     * interface for put method
     *
     * @param key
     * @param value
     * @param expirationInSeconds
     */
    void put(String key, Object value, long expirationInSeconds);

    /**
     * interface for getting from cache
     *
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * interface for evicting a key from cache
     *
     * @param key
     */
    void evict(String key);

    /** interface for evicting all keys from cache */
    void evictReportCache();
}
