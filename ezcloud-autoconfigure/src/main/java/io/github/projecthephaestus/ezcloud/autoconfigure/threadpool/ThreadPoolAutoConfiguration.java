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

package io.github.projecthephaestus.ezcloud.autoconfigure.threadpool;

import io.github.projecthephaestus.ezcloud.autoconfigure.AutoConfigConstants;
import io.github.projecthephaestus.ezcloud.autoconfigure.threadpool.properties.CommonTaskSchedulerProperties;
import io.github.projecthephaestus.ezcloud.autoconfigure.threadpool.properties.CommonTaskExecutorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author CloudS3n
 * @date 2021-06-11 10:11
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({CommonTaskExecutorProperties.class, CommonTaskSchedulerProperties.class})
@ConditionalOnProperty(prefix = AutoConfigConstants.CONFIG_PREFIX, name = "enable-thread-pool", havingValue = "true")
public class ThreadPoolAutoConfiguration {

    static {
        log.info(AutoConfigConstants.LOADING_THREAD_POOL_AUTO_CONFIGURE);
    }

    private final CommonTaskExecutorProperties commonTaskExecutorProperties;
    private final CommonTaskSchedulerProperties commonTaskSchedulerProperties;

    @Bean
    @ConditionalOnMissingBean(name = "commonTaskExecutor")
    public ThreadPoolTaskExecutor commonTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(commonTaskExecutorProperties.getCorePoolSize());
        executor.setMaxPoolSize(commonTaskExecutorProperties.getMaxPoolSize());
        executor.setQueueCapacity(commonTaskExecutorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(commonTaskExecutorProperties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(commonTaskExecutorProperties.getAllowCoreThreadTimeout());
        executor.setKeepAliveSeconds((int) commonTaskExecutorProperties.getKeepAliveSeconds().getSeconds());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(name = "commonTaskScheduler")
    public ThreadPoolTaskScheduler commonTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(commonTaskSchedulerProperties.getPoolSize());
        scheduler.setThreadNamePrefix(commonTaskSchedulerProperties.getThreadNamePrefix());
        scheduler.initialize();
        return scheduler;
    }
}
