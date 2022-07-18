package org.athena.config.exception;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.athena.common.exception.BusinessException;
import org.athena.common.exception.InternalServerError;
import org.athena.common.resp.ErrorResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * 业务异常处理器
 */
public final class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    private static Logger logger = LoggerFactory.getLogger(BusinessExceptionMapper.class);

    private final Meter exceptions;

    public BusinessExceptionMapper(MetricRegistry metrics) {
        exceptions = metrics.meter(MetricRegistry.name(getClass(), "business exceptions"));
    }

    @Override
    public Response toResponse(BusinessException exception) {
        exceptions.mark();
        logger.error("BusinessException type: {} status: {} code: {} message: {}", exception.getClass().getName(),
                exception.getStatus(), exception.getCode(), exception.getMessage());
        ErrorResp errorResp = ErrorResp.builder().code(exception.getCode())
                .message(exception instanceof InternalServerError
                        ? Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase() : exception.getMessage())
                .build();
        return Response.status(exception.getStatus())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorResp)
                .build();
    }

}
