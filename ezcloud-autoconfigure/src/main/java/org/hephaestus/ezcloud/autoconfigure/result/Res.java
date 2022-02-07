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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一响应JSON对象
 *
 * @author clouds3n
 * @since 2021-12-12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Res<T> implements Serializable {

    private static final long serialVersionUID = -709109828798783625L;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误代码
     */
    private String code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    private static final String OPT_SUCCESS = "操作成功";
    private static final String OPT_ERROR = "操作失败";

    public static <T> Res<T> ok() {
        return ok(OPT_SUCCESS);
    }

    public static <T> Res<T> ok(T data) {
        return ok(OPT_SUCCESS, data);
    }

    public static <T> Res<T> ok(String msg) {
        return ok(msg, null);
    }

    public static <T> Res<T> ok(String msg, T data) {
        return new Res<T>().setSuccess(true).setMsg(msg).setData(data);
    }

    public static <T> Res<T> error() {
        return new Res<T>().setSuccess(false).setMsg(OPT_ERROR);
    }

    public static <T> Res<T> error(String msg) {
        return error(CommonErrorCode.INTERNAL_ERROR.getCode(), msg);
    }

    public static <T> Res<T> error(T data) {
        return error(CommonErrorCode.INTERNAL_ERROR.getCode(), null ,data);
    }

    public static <T> Res<T> error(String code, String msg) {
        return error(code, msg, null);
    }

    public static <T> Res<T> error(String msg, T data) {
        return error(CommonErrorCode.INTERNAL_ERROR.getCode(), msg, data);
    }

    public static <T> Res<T> error(String code, String msg, T data) {
        return new Res<T>().setSuccess(false).setCode(code).setMsg(msg).setData(data);
    }

    public static <T> Res<T> unauthorized() {
        return new Res<T>().setSuccess(false).setCode(CommonErrorCode.UNAUTHORIZED.getCode()).setMsg(CommonErrorCode.UNAUTHORIZED.getMsg());
    }

    public static <T> Res<T> accessDenied() {
        return new Res<T>().setSuccess(false).setCode(CommonErrorCode.ACCESS_DENIED.getCode()).setMsg(CommonErrorCode.ACCESS_DENIED.getMsg());
    }
}
