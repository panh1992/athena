package org.athena.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.athena.config.redis.RedisConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class AthenaConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private String defaultName;

    private SwaggerBundleConfiguration swagger;

    private CorsConfig cors;

    @Valid
    @NotNull
    private SnowflakeConfig snowflake;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private RedisConfig redis = new RedisConfig();

}
