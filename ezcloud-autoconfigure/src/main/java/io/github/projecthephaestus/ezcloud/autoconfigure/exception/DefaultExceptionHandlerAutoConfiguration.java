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

package io.github.projecthephaestus.ezcloud.autoconfigure.exception;

import io.github.projecthephaestus.ezcloud.autoconfigure.AutoConfigConstants;
import io.github.projecthephaestus.ezcloud.autoconfigure.result.CommonErrorCode;
import io.github.projecthephaestus.ezcloud.autoconfigure.result.ErrorCode;
import io.github.projecthephaestus.ezcloud.autoconfigure.result.Res;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:57
 */
@Slf4j
@RestController
@ControllerAdvice
@ConditionalOnProperty(prefix = AutoConfigConstants.CONFIG_PREFIX, name = "enable-exception-handler", havingValue = "true")
public class DefaultExceptionHandlerAutoConfiguration {

    static {
        log.info(AutoConfigConstants.LOADING_EXCEPTION_HANDLER_AUTO_CONFIGURE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Res<?> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        StringBuilder msg = new StringBuilder();
        e.getBindingResult().getAllErrors().stream().limit(1).forEach(error -> msg.append(error.getDefaultMessage()).append(","));
        return Res.error(StringUtils.isBlank(msg.toString()) ? AutoConfigConstants.SERVER_ERROR : msg.toString());
    }

    @ExceptionHandler(BusinessException.class)
    public Res<?> uniExceptionHandler(BusinessException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        ErrorCode commonErrorCode = Optional.ofNullable(e.getErrorCode()).orElse(CommonErrorCode.INTERNAL_ERROR);
        return Res.error(commonErrorCode.getCode(), commonErrorCode.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public Res<?> otherExceptionHandler(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return Res.error(AutoConfigConstants.ERROR_OPERATE);
    }

    private Res<?> unauthorizedHandler(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return Res.unauthorized();
    }
}
