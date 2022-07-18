package org.athena.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisUtil {

    private static RedissonClient client;

    /**
     * 获取 RedisClient
     */
    public static RedissonClient getClient(String address, String password, Integer db) {
        if (client == null) {
            synchronized (RedisUtil.class) {
                if (client == null) {
                    Config config = new Config();
                    config.setTransportMode(TransportMode.NIO);
                    config.useSingleServer().setAddress(address);
                    config.useSingleServer().setPassword(password);
                    config.useSingleServer().setDatabase(db);
                    client = Redisson.create(config);
                }
            }
        }
        return client;
    }

    /**
     * 获取redis存储的字符串
     *
     * @param key 存储键
     * @return 存储的字符串
     */
    public static String getString(String key) {
        RBucket<String> test = client.getBucket(key);
        return test.isExists() ? test.get() : null;
    }

    /**
     * 存储字符串
     *
     * @param key   存储键
     * @param value 存储值
     */
    public static void setString(String key, String value) {
        client.getBucket(key).set(value);
    }

    /**
     * 存储字符串，在指定时间后失效
     *
     * @param key     存储键
     * @param value   存储值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public static void setString(String key, String value, long timeout, TimeUnit unit) {
        client.getBucket(key).set(value, timeout, unit);
    }

    /**
     * 删除某存储键
     *
     * @param key 存储键
     */
    public static void delete(String key) {
        client.getBucket(key).delete();
    }

    /**
     * 对某一存储键, 添加分布式锁
     *
     * @param key     存储键
     * @param timeout 过期时间（-1：为不过期，需要调用 unlock 进行解锁）
     * @param unit    时间单位
     */
    public static void lock(String key, long timeout, TimeUnit unit) {
        client.getLock(key).lock(timeout, unit);
    }

    /**
     * 检查分布式锁是否被锁定
     *
     * @param key 存储键
     * @return true: 锁定   false: 未锁定
     */
    public static boolean isLocked(String key) {
        return client.getLock(key).isLocked();
    }

    /**
     * 对某一存储键, 解除分布式锁
     *
     * @param key 存储键
     */
    public static void unlock(String key) {
        client.getLock(key).unlock();
    }

}
