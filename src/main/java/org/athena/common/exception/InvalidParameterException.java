package org.athena.common.exception;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * 参数错误异常
 */
@Getter
public class InvalidParameterException extends BusinessException {

    private InvalidParameterException(Response.Status status, String code, String message) {
        super(status, code, message);
    }

    public static InvalidParameterException build() {
        return new InvalidParameterException(Response.Status.BAD_REQUEST, "InvalidParameter", "参数验证失败");
    }

    public static InvalidParameterException build(String message) {
        return new InvalidParameterException(Response.Status.BAD_REQUEST, "InvalidParameter", message);
    }

    public static InvalidParameterException build(Response.Status status, String message) {
        return new InvalidParameterException(status, "InvalidParameter", message);
    }

    public static InvalidParameterException build(String code, String message) {
        return new InvalidParameterException(Response.Status.BAD_REQUEST, code, message);
    }

    public static InvalidParameterException build(Response.Status status, String code, String message) {
        return new InvalidParameterException(status, code, message);
    }

}
