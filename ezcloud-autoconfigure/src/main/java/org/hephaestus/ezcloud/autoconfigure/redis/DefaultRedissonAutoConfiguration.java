/*
 * MIT License
 *
 * Copyright (c) 2020 CloudSen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.hephaestus.ezcloud.autoconfigure.redis;

import org.hephaestus.ezcloud.autoconfigure.AutoConfigConstants;
import org.hephaestus.ezcloud.autoconfigure.redis.properties.UniRedissonProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Redisson客户端的Spring Cache配置
 *
 * @author CloudS3n
 * @date 2021-06-16 13:31
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(UniRedissonProperties.class)
@ConditionalOnBean({RedisOperations.class, RedissonClient.class})
@ConditionalOnProperty(name = "uni.autoconfigure.enable-redisson-cache-manager", havingValue = "true")
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@SuppressWarnings({"RedundantThrows", "SpringJavaInjectionPointsAutowiringInspection"})
public class DefaultRedissonAutoConfiguration {

    static {
        log.info(AutoConfigConstants.LOADING_REDISSON_AUTO_CONFIGURE);
    }

    private final UniRedissonProperties uniRedissonProperties;

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) throws IOException {
        log.info(AutoConfigConstants.LOADING_REDISSON_CACHE_MGMT_AUTO_CONFIGURE);
        Map<String, CacheConfig> cacheConfigMap = new HashMap<>(16);
        Map<String, UniRedissonProperties.RedissonCacheManagerExpireTime> cacheManagerExpireTimeMap =
            uniRedissonProperties.getCacheManagerExpireTimeMap();
        if (MapUtils.isNotEmpty(cacheManagerExpireTimeMap)) {
            cacheManagerExpireTimeMap.forEach((k, v) -> cacheConfigMap.put(k, new CacheConfig(v.getTtl().toMillis(), v.getMaxIdleTime().toMillis())));
        }
        return new RedissonSpringCacheManager(redissonClient, cacheConfigMap);
    }
}
