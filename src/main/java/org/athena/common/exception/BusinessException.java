package org.athena.common.exception;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * 业务异常， 所有自定义异常需要继承此异常
 */
@Getter
public abstract class BusinessException extends RuntimeException {

    final Response.Status status;

    final String code;

    BusinessException(Response.Status status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

}
