package org.athena.common.exception;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * 无权限异常
 */
@Getter
public class AuthenticateException extends BusinessException {

    private AuthenticateException(Response.Status status, String code, String message) {
        super(status, code, message);
    }

    public static AuthenticateException build() {
        return new AuthenticateException(Response.Status.UNAUTHORIZED, "Unauthorized", "无权限访问");
    }

}
