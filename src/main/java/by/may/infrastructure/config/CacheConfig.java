package by.may.infrastructure.config;

import jakarta.persistence.Cacheable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@Cacheable
@EnableCaching
public class CacheConfig {
    public static final String CATEGORIES_LIST  = "categoriesList";
    public static final String CATEGORY_BY_ID   = "categoryById";
    public static final String CATEGORY_BY_NAME = "categoryByName";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CATEGORIES_LIST, CATEGORY_BY_ID, CATEGORY_BY_NAME);
    }
}
