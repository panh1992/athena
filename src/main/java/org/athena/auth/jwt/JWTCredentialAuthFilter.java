package org.athena.auth.jwt;

import io.dropwizard.auth.AuthFilter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.athena.account.business.ResourceBusiness;
import org.athena.auth.UserInfo;
import org.athena.common.exception.AuthenticateException;
import org.athena.common.util.Constant;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;

import javax.annotation.Nullable;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

/**
 * JWT token 过滤器, 用户进行资源鉴权校验
 */
@Builder
@Priority(Priorities.AUTHENTICATION)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JWTCredentialAuthFilter extends AuthFilter<JwtClaims, UserInfo> {

    private ResourceBusiness resourceBusiness;

    private JWTAuthenticator jwtAuthenticator;

    private JWTAuthorizer jwtAuthorizer;

    private String[] excludeUri;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String uri = "/".concat(requestContext.getUriInfo().getPath());
        for (String s : excludeUri) {
            if (s.startsWith(uri)) {
                return;
            }
        }
        // 判断请求资源是否进行鉴权
        if (!resourceBusiness.isVerify(uri.replaceAll("\\{.*?}", "*"), requestContext.getMethod())) {
            final JwtClaims credentials =
                    getCredentials(requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            if (!authenticate(requestContext, credentials, "JWT")) {
                throw AuthenticateException.build();
            }
        }
    }

    @Nullable
    private JwtClaims getCredentials(String jwtToken) {
        try {
            return JWTUtil.validation(jwtToken, Constant.AUTHORIZATION_DURATION);
        } catch (InvalidJwtException e) {
            logger.error("认证信息解析失败, Authorization token: {}, Exception Message: {}", jwtToken, e.getMessage());
            return null;
        }
    }

    @Override
    protected boolean authenticate(ContainerRequestContext requestContext, JwtClaims credentials, String scheme) {

        if (Objects.isNull(credentials)) {
            return false;
        }

        final Optional<UserInfo> principal = jwtAuthenticator.authenticate(credentials);
        if (principal.isEmpty()) {
            return false;
        }
        UserInfo userInfo = principal.get();

        final SecurityContext securityContext = requestContext.getSecurityContext();
        final boolean secure = Objects.nonNull(securityContext) && securityContext.isSecure();

        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {
                return userInfo;
            }

            @Override
            public boolean isUserInRole(String role) {
                return jwtAuthorizer.authorize(userInfo, role);
            }

            @Override
            public boolean isSecure() {
                return secure;
            }

            @Override
            public String getAuthenticationScheme() {
                return scheme;
            }

        });
        return true;
    }

}
