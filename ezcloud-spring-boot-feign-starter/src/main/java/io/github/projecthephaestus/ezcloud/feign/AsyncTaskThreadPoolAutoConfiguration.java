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

package io.github.projecthephaestus.ezcloud.feign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.github.projecthephaestus.ezcloud.autoconfigure.AutoConfigConstants;
import io.github.projecthephaestus.ezcloud.feign.properties.AsyncTaskExecutorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author CloudS3n
 * @date 2021-07-26 14:15
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({AsyncTaskExecutorProperties.class})
@ConditionalOnProperty(prefix = AutoConfigConstants.CONFIG_PREFIX, name = "enable-thread-pool", havingValue = "true")
@ConditionalOnClass(FeignClient.class)
public class AsyncTaskThreadPoolAutoConfiguration {

    static {
        log.info("[ 自动装配 ] 加载异步任务线程池配置...");
    }

    private final AsyncTaskExecutorProperties asyncTaskExecutorProperties;

    @Bean
    @ConditionalOnMissingBean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncTaskExecutorProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncTaskExecutorProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncTaskExecutorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(asyncTaskExecutorProperties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(asyncTaskExecutorProperties.getAllowCoreThreadTimeout());
        executor.setKeepAliveSeconds((int) asyncTaskExecutorProperties.getKeepAliveSeconds().getSeconds());
        executor.setTaskDecorator(new AsyncTaskDecorator());
        executor.initialize();
        return executor;
    }
}
