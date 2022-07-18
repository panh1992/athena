package org.athena.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.athena.common.util.SnowflakeIdWorker;
import org.athena.config.configuration.AthenaConfiguration;

public class EnvironmentModule extends AbstractModule {

    @Inject
    @Provides
    @Singleton
    public SnowflakeIdWorker snowflakeIdWorker(AthenaConfiguration configuration) {
        return new SnowflakeIdWorker(configuration.getSnowflake().getWorkerId(),
                configuration.getSnowflake().getDataCenterId());
    }

}
