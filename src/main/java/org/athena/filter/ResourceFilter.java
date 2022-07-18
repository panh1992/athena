package org.athena.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 资源过滤器，用于资源、操作审计、 操作
 */
public class ResourceFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(ResourceFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("ResourceFilter 开始");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        logger.info("URI: {}, METHOD: {}", uri, method);

        chain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
        logger.info("ResourceFilter 销毁");
    }
}
