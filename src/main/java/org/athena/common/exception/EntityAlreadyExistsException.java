package org.athena.common.exception;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * 实体已存在异常
 */
@Getter
public class EntityAlreadyExistsException extends BusinessException {

    private EntityAlreadyExistsException(Response.Status status, String code, String message) {
        super(status, code, message);
    }

    public static EntityAlreadyExistsException build(String message) {
        return new EntityAlreadyExistsException(Response.Status.CONFLICT, "EntityAlreadyExists", message);
    }

    public static EntityAlreadyExistsException build(String code, String message) {
        return new EntityAlreadyExistsException(Response.Status.CONFLICT, code, message);
    }

    public static EntityAlreadyExistsException build(Response.Status status, String code, String message) {
        return new EntityAlreadyExistsException(status, code, message);
    }

}
