package org.athena.config.configuration;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CorsConfiguration {

    @NotNull
    private String allowedOrigins;

    @NotNull
    private String allowedHeaders;

    @NotNull
    private String allowedMethods;

    @NotNull
    private String[] urlPatterns;

    public String[] getUrlPatterns() {
        return urlPatterns.clone();
    }

    public void setUrlPatterns(String[] urlPatterns) {
        this.urlPatterns = urlPatterns.clone();
    }
}
