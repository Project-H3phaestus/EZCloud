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

package io.github.projecthephaestus.ezcloud.autoconfigure.es;

import io.github.projecthephaestus.ezcloud.autoconfigure.AutoConfigConstants;
import io.github.projecthephaestus.ezcloud.autoconfigure.es.properties.EsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:58
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({EsProperties.class})
@ConditionalOnClass(RestHighLevelClient.class)
@ConditionalOnProperty(prefix = AutoConfigConstants.CONFIG_PREFIX, name = "enable-es", havingValue = "true")
public class EsAutoConfiguration {

    public EsAutoConfiguration() {
        log.info(AutoConfigConstants.LOADING_ES_AUTO_CONFIGURE);
    }

    private static InetAddress parseAddress(String s) {
        try {
            return InetAddress.getByName(s);
        } catch (UnknownHostException e) {
            log.error(AutoConfigConstants.ERROR_ES_CAN_NOT_PARSE_ADDRESS, ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestHighLevelClient esClient(EsProperties properties) {
        return new RestHighLevelClient(
            RestClient.builder(
                properties.getAddresses().stream()
                    .map(EsAutoConfiguration::parseAddress).filter(Objects::nonNull)
                    .map(address -> new HttpHost(address, properties.getRestPort()))
                    .collect(Collectors.toList())
                    .toArray(new HttpHost[properties.getAddresses().size()])
            )
        );
    }

    @Bean
    @ConditionalOnProperty(name = "uni.config.es.enable-sniff", havingValue = "true")
    @ConditionalOnMissingBean
    public Sniffer sniffer(RestHighLevelClient esClient) {
        return Sniffer.builder(esClient.getLowLevelClient()).build();
    }
}
