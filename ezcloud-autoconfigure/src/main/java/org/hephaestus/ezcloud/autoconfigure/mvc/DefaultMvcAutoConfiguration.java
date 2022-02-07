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

package org.hephaestus.ezcloud.autoconfigure.mvc;

import org.hephaestus.ezcloud.autoconfigure.AutoConfigConstants;
import org.hephaestus.ezcloud.autoconfigure.mvc.intercepter.DefaultLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author CloudS3n
 * @date 2021-06-15 12:06
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
@ConditionalOnProperty(name = "uni.autoconfigure.enable-mvc", havingValue = "true")
public class DefaultMvcAutoConfiguration implements WebMvcConfigurer {

    static {
        log.info(AutoConfigConstants.LOADING_MVC_AUTO_CONFIGURE);
    }

    private final DefaultLoggingInterceptor defaultLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(defaultLoggingInterceptor)
            .excludePathPatterns("/*/*.html",
                "/*/*.css",
                "/*/*.js",
                "/*/*.png",
                "/swagger*/**",
                "/**/error*");
    }
}
