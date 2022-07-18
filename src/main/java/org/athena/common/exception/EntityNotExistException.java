package org.athena.common.exception;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * 实体未找到异常
 */
@Getter
public class EntityNotExistException extends BusinessException {

    private EntityNotExistException(Response.Status status, String code, String message) {
        super(status, code, message);
    }

    public static EntityNotExistException build(String message) {
        return new EntityNotExistException(Response.Status.NOT_FOUND, "EntityNotExist", message);
    }

    public static EntityNotExistException build(String code, String message) {
        return new EntityNotExistException(Response.Status.NOT_FOUND, code, message);
    }

    public static EntityNotExistException build(Response.Status status, String code, String message) {
        return new EntityNotExistException(status, code, message);
    }

}
