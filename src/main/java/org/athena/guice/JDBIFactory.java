package org.athena.guice;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.athena.config.configuration.AthenaConfiguration;
import org.athena.plugin.jdbi.InstantPlugin;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jpa.JpaPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JDBIFactory {

    /**
     * 获取 jdbi 句柄
     */
    public static Jdbi get(AthenaConfiguration configuration, Environment environment) {
        Jdbi jdbi = Jdbi.create(buildDataSource(configuration.getDatabase(), environment));
        jdbi.installPlugin(new JpaPlugin());
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new InstantPlugin());
        return jdbi;
    }

    private static HikariDataSource buildDataSource(DataSourceFactory factory, Environment environment) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(factory.getDriverClass());
        config.setJdbcUrl(factory.getUrl());
        config.setUsername(factory.getUser());
        config.setPassword(factory.getPassword());
        config.setMinimumIdle(factory.getMinSize());
        config.setMaximumPoolSize(factory.getMaxSize());
        config.setConnectionTestQuery(factory.getValidationQuery().orElse(null));
        // 是否自定义配置，为true时下面两个参数才生效
        config.addDataSourceProperty("cachePrepStmts", "true");
        // 连接池大小默认25，官方推荐250-500
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        // 单条语句最大长度默认256，官方推荐2048
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        // 新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setMetricRegistry(environment.metrics());
        config.setHealthCheckRegistry(environment.healthChecks());
        return new HikariDataSource(config);
    }

}
