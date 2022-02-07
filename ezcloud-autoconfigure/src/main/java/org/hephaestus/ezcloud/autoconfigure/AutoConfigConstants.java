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

package org.hephaestus.ezcloud.autoconfigure;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:59
 */
public final class AutoConfigConstants {

    public static final String PREFIX = "[ 自动装配 ] ";
    public static final String LOADING_ES_AUTO_CONFIGURE = PREFIX + "加载ES配置";
    public static final String ERROR_ES_CAN_NOT_PARSE_ADDRESS = PREFIX + "解析ES地址异常, {}";
    public static final String LOADING_THREAD_POOL_AUTO_CONFIGURE = PREFIX + "加载默认线程池配置";
    public static final String LOADING_MVC_AUTO_CONFIGURE = PREFIX + "加载默认MVC配置";
    public static final String LOADING_EXCEPTION_HANDLER_AUTO_CONFIGURE = PREFIX + "加载默认全局异常处理配置";
    public static final String LOADING_SERIALIZER_AUTO_CONFIGURE = PREFIX + "加载默认Jackson序列化配置";
    public static final String LOADING_OLD_REDIS_AUTO_CONFIGURE = PREFIX + "加载旧的Redis配置";
    public static final String LOADING_OLD_REDIS_TEMPLATE = PREFIX + "加载RedisTemplate配置";
    public static final String LOADING_OLD_REDIS_SERIALIZER = PREFIX + "加载Redis序列化配置";
    public static final String LOADING_OLD_REDIS_CACHE = PREFIX + "加载RedisCache配置";
    public static final String LOADING_REDISSON_AUTO_CONFIGURE = PREFIX + "加载Redisson配置";
    public static final String LOADING_REDISSON_CACHE_MGMT_AUTO_CONFIGURE = PREFIX + "加载Redisson cache manager配置";
    public static final String LOADING_TRANSACTION_AUTO_CONFIGURE = PREFIX + "加载MP事务配置";
    public static final String LOADING_MYBATIS_PLUS_AUTO_CONFIGURE = PREFIX + "加载MP配置";
    public static final String ERROR_OPERATE = "操作失败";
    public static final String SERVER_ERROR = "服务端错误";
    public static final String NO_AUTHORIZATION = "没有权限";
    public static final String TRUE = "true";
}
