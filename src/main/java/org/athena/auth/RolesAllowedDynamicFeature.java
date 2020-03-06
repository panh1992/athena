package org.athena.auth;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.athena.account.business.ResourceBusiness;
import org.athena.account.business.RoleBusiness;
import org.glassfish.jersey.server.model.AnnotatedMethod;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import java.util.List;
import java.util.Objects;

public class RolesAllowedDynamicFeature implements DynamicFeature {

    private RoleBusiness roleBusiness;

    private ResourceBusiness resourceBusiness;

    public RolesAllowedDynamicFeature(RoleBusiness roleBusiness, ResourceBusiness resourceBusiness) {
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
