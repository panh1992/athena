package org.athena.common.exception;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * 系统错误
 */
@Getter
public class InternalServerError extends BusinessException {

    private InternalServerError(Response.Status status, String code, String message) {
        super(status, code, message);
    }

    public static InternalServerError build(String code, String message) {
        return new InternalServerError(Response.Status.INTERNAL_SERVER_ERROR, code, message);
    }

    public static InternalServerError build(String message) {
        return new InternalServerError(Response.Status.INTERNAL_SERVER_ERROR,
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), message);
    }

}
