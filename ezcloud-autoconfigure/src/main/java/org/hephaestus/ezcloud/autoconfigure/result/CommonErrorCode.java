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

package org.hephaestus.ezcloud.autoconfigure.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 通用错误码
 *
 * @author clouds3n
 * @since 2021-12-13
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    /**
     * 系统未知错误 COMMON_00000
     */
    INTERNAL_ERROR(COMMON_CODE, COMMON_MSG),
    TOKEN_INVALIDED("COMMON_00001", "无法获取用户信息，请确保token的有效性"),
    /**
     * 认证失败 COMMON_00401
     */
    UNAUTHORIZED("COMMON_00401", "认证失败"),
    /**
     * 无操作权限 COMMON_00403
     */
    ACCESS_DENIED("COMMON_00403", "无操作权限"),
    ;

    private final String code;
    private final String msg;

    public static String parseMsg(String code) {
        return Arrays.stream(values())
            .filter(e -> StringUtils.equals(e.code, code))
            .findAny()
            .map(CommonErrorCode::getMsg)
            .orElse(COMMON_MSG);
    }

    public static ErrorCode parseEnum(String code) {
        return Arrays.stream(values())
            .filter(e -> StringUtils.equals(e.code, code))
            .findAny()
            .orElse(INTERNAL_ERROR);
    }
}
