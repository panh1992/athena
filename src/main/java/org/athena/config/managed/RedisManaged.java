package org.athena.config.managed;

import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import org.athena.common.util.RedisUtil;
import org.athena.config.configuration.AthenaConfiguration;
import org.athena.config.configuration.RedisConfiguration;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisManaged implements Managed {

    private static Logger logger = LoggerFactory.getLogger(RedisManaged.class);

    private RedissonClient client;

    private RedisConfiguration config;

    @Inject
    public RedisManaged(AthenaConfiguration config) {
        this.config = config.getRedis();
    }

    @Override
    public void start() {
        logger.info("Redis - Starting...");
        this.client = RedisUtil.getClient(config.getAddress(), config.getPassword(), config.getDb());
        logger.info("Redis - Start completed");
    }

    @Override
    public void stop() {
        logger.info("Redis - Shutdown initiated...");
        this.client.shutdown();
        logger.info("Redis - Shutdown completed");
    }

}
