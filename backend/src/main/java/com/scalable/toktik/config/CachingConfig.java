package com.scalable.toktik.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

//https://tech.groww.in/implementing-multiple-ttl-with-caffeine-cache-in-springboot-b1a1874e19a
@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache userInfoCache = buildCache("UserInfo", 5, TimeUnit.MINUTES);
        CaffeineCache videoInfo = buildCache("VideoInfo", 1, TimeUnit.SECONDS, 1000);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(userInfoCache, videoInfo));
        return manager;
    }

    private CaffeineCache buildCache(String name, @NonNegative int expireTime, TimeUnit unit, int maxSize) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(maxSize)
                .expireAfterAccess(expireTime, unit)
                .build());
    }

    private CaffeineCache buildCache(String name, @NonNegative int expireTime, TimeUnit unit) {
        return buildCache(name, expireTime, unit, 100);
    }


}
