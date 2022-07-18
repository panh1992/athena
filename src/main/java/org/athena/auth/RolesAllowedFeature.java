package org.athena.auth;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.athena.account.business.ResourceBusiness;
import org.athena.account.business.RoleBusiness;
import org.glassfish.jersey.server.model.AnnotatedMethod;

import javax.ws.rs.*;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import java.util.List;
import java.util.Objects;

/**
 * 权限校验处理 Feature
 */
public class RolesAllowedFeature implements DynamicFeature {

    private RoleBusiness roleBusiness;

    private ResourceBusiness resourceBusiness;

    public RolesAllowedFeature(RoleBusiness roleBusiness, ResourceBusiness resourceBusiness) {
        this.roleBusiness = roleBusiness;
        this.resourceBusiness = resourceBusiness;
    }

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext configuration) {
        final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
        List<String> methods = Lists.newArrayList();
        if (Objects.nonNull(am.getAnnotation(OPTIONS.class))) {
            methods.add("OPTIONS");
        }
        if (Objects.nonNull(am.getAnnotation(HEAD.class))) {
            methods.add("HEAD");
        }
        if (Objects.nonNull(am.getAnnotation(GET.class))) {
            methods.add("GET");
        }
        if (Objects.nonNull(am.getAnnotation(POST.class))) {
            methods.add("POST");
        }
        if (Objects.nonNull(am.getAnnotation(PUT.class))) {
            methods.add("PUT");
        }
        if (Objects.nonNull(am.getAnnotation(PATCH.class))) {
            methods.add("PATCH");
        }
        if (Objects.nonNull(am.getAnnotation(DELETE.class))) {
            methods.add("DELETE");
        }
        if (CollectionUtils.isNotEmpty(methods)) {
            Path path = am.getAnnotation(Path.class);
            String uri = Objects.nonNull(path) ? path.value() : "";
            Path parentPath = resourceInfo.getResourceClass().getAnnotation(Path.class);
            uri = Objects.nonNull(parentPath) ? parentPath.value().concat(uri) : uri;
            configuration.register(new RolesAllowedRequestFilter(uri, roleBusiness, resourceBusiness));
        }
    }

}
