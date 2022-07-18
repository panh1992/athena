package org.athena.config;

import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.jdbi3.JdbiHealthCheck;
import io.dropwizard.setup.Environment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.athena.account.business.ResourceBusiness;
import org.athena.account.business.RoleBusiness;
import org.athena.account.business.UserBusiness;
import org.athena.auth.AthenaDynamicFeature;
import org.athena.auth.RolesAllowedFeature;
import org.athena.auth.UserInfo;
import org.athena.auth.jwt.JWTAuthenticator;
import org.athena.auth.jwt.JWTAuthorizer;
import org.athena.auth.jwt.JWTCredentialAuthFilter;
import org.athena.config.configuration.AthenaConfiguration;
import org.athena.config.configuration.CorsConfiguration;
import org.athena.config.exception.BusinessExceptionMapper;
import org.athena.config.exception.ValidationExceptionMapper;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * 环境配置类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvConfig {

    /**
     * 开启 CORS 跨域
     */
    public static void registerCors(Environment environment, CorsConfiguration cors) {

        final FilterRegistration.Dynamic dynamic =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        dynamic.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, cors.getAllowedOrigins());
        dynamic.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, cors.getAllowedHeaders());
        dynamic.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, cors.getAllowedMethods());

        dynamic.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, cors.getUrlPatterns());

    }

    /**
     * 注册异常处理类
     */
    public static void registerException(Environment environment) {

        environment.jersey().register(new BusinessExceptionMapper(environment.metrics()));

        environment.jersey().register(new ValidationExceptionMapper());

    }

    /**
     * 注册认证鉴权处理
     */
    public static void registerAuthorization(Environment environment, GuiceBundle guiceBundle) {

        UserBusiness userBusiness = guiceBundle.getInjector().getInstance(UserBusiness.class);
        ResourceBusiness resourceBusiness = guiceBundle.getInjector().getInstance(ResourceBusiness.class);
        // 添加鉴权校验
        environment.jersey().register(new AthenaDynamicFeature(JWTCredentialAuthFilter.builder()
                .jwtAuthenticator(new JWTAuthenticator(userBusiness)).jwtAuthorizer(new JWTAuthorizer(userBusiness))
                .resourceBusiness(resourceBusiness).excludeUri(new String[]{"/swagger", "/swagger.json", "/login", "/register"})
                .build()));

        // 添加权限校验
        environment.jersey().register(new RolesAllowedFeature(guiceBundle.getInjector()
                .getInstance(RoleBusiness.class), resourceBusiness));
        // If you want to use @Auth to inject a custom Principal type into your resource
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(UserInfo.class));

    }

    /**
     * 注册健康检查
     */
    public static void registerHealthCheck(Environment environment, AthenaConfiguration configuration,
                                           GuiceBundle guiceBundle) {

        environment.healthChecks().register("dataBaseHealthCheck", new JdbiHealthCheck(guiceBundle
                .getInjector().getInstance(Jdbi.class), configuration.getDatabase().getValidationQuery()));

    }

}
