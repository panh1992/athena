package org.athena.config.configuration;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Twitter_Snowflake 配置类
 */
@Data
public class SnowflakeConfiguration {

    @Min(0)
    @Max(31)
    @NotNull
    private long workerId = 0;

    @Min(0)
    @Max(31)
    @NotNull
    private long dataCenterId = 0;

}
