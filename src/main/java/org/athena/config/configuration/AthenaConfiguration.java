package org.athena.config.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jobs.JobConfiguration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class AthenaConfiguration extends Configuration implements JobConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private String defaultName;

    private SwaggerBundleConfiguration swagger;

    private CorsConfiguration cors;

    private Map<String, String> quartz;

    @Valid
    @NotNull
    private SnowflakeConfiguration snowflake;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private RedisConfiguration redis = new RedisConfiguration();

    @Override
    public Map<String, String> getJobs() {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> getQuartzConfiguration() {
        return this.quartz;
    }

}
