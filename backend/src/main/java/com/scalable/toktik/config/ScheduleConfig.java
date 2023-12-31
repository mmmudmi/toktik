package com.scalable.toktik.config;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;

//@EnableScheduling
//@Component
public class ScheduleConfig {
    @CacheEvict(cacheNames = {"video", "comment"})
    @Scheduled(fixedRateString = "${caching.spring.expireTime}")
    public void emptyCache() {
        System.out.println("emptying cache");
    }
}
