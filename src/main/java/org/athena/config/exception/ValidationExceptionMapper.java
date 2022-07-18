package org.athena.config.exception;

import com.google.common.collect.Maps;
import io.dropwizard.jersey.validation.ConstraintMessage;
import io.dropwizard.jersey.validation.JerseyViolationException;
import org.athena.common.resp.ErrorResp;
import org.glassfish.jersey.server.model.Invocable;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Map;
import java.util.Set;

/**
 * 参数校验异常 处理器
 */
public final class ValidationExceptionMapper implements ExceptionMapper<JerseyViolationException> {

    @Override
    public Response toResponse(JerseyViolationException exception) {

        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        final Invocable invocable = exception.getInvocable();

        Map<String, String> errorResult = Maps.newHashMap();

        for (ConstraintViolation<?> violation : violations) {
            String[] x = ConstraintMessage.getMessage(violation, invocable).split("\\s");
            if (x.length > 1) {
                String[] info = new String[x.length - 1];
                System.arraycopy(x, 1, info, 0, info.length);
                errorResult.put(x[0], String.join("", info));
            }
        }

        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ErrorResp.builder().code("InvalidParameterException").data(errorResult)
                        .message("参数验证失败").build()).build();

    }

}
