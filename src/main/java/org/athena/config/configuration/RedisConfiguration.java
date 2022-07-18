package org.athena.config.configuration;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class RedisConfiguration {

    @NotEmpty
    private String address;

    private String password;

    @Min(0)
    @Max(15)
    private int db = 0;

}
