package org.aibles.spaced_repetition.flashcard.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        log.info("Configuring flashcard cache manager");

        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();

        // Define cache names for flashcard service
        cacheManager.setCacheNames(java.util.Arrays.asList(
                "flashcard-cache",           // Individual flashcard cache
                "flashcards-list-cache",     // Paginated list cache
                "deck-flashcard-count-cache" // Deck count cache
        ));

        cacheManager.setAllowNullValues(false);

        log.info("Cache manager configured with caches: {}", cacheManager.getCacheNames());
        return cacheManager;
    }
}