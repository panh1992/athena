package org.athena.auth;

import org.athena.account.business.ResourceBusiness;
import org.athena.account.business.RoleBusiness;
import org.athena.account.entity.Resource;
import org.athena.account.entity.Role;
import org.athena.common.exception.AuthenticateException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.util.List;
import java.util.Optional;

@Priority(Priorities.AUTHORIZATION)
public class RolesAllowedRequestFilter implements ContainerRequestFilter {

    private String uri;

    private RoleBusiness roleBusiness;

    private ResourceBusiness resourceBusiness;

    RolesAllowedRequestFilter(String uri, RoleBusiness roleBusiness, ResourceBusiness resourceBusiness) {
        this.uri = uri;
        this.roleBusiness = roleBusiness;
        this.resourceBusiness = resourceBusiness;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {

        Optional<Resource> resourceOptional = resourceBusiness.get(this.uri.replaceAll("\\{.+?}", "*"),
                requestContext.getMethod());
        if (resourceOptional.isPresent()) {
            if (!isAuthenticated(requestContext)) {
                throw AuthenticateException.build();
            }
            Resource resource = resourceOptional.get();
            List<Role> rolesAllowed = roleBusiness.getByResourceId(resource.getResourceId());
            for (Role role : rolesAllowed) {
                if (requestContext.getSecurityContext().isUserInRole(role.getName())) {
                    return;
                }
            }
            throw AuthenticateException.build();
        }
    }

    private static boolean isAuthenticated(final ContainerRequestContext requestContext) {

        return requestContext.getSecurityContext().getUserPrincipal() != null;

    }

}
