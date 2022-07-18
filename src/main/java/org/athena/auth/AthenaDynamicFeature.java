package org.athena.auth;

import org.athena.auth.jwt.JWTCredentialAuthFilter;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

public class AthenaDynamicFeature implements DynamicFeature {

    private final ContainerRequestFilter authFilter;

    public AthenaDynamicFeature(JWTCredentialAuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {

        context.register(authFilter);

    }

}
