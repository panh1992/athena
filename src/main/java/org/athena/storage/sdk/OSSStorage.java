package org.athena.storage.sdk;

import java.io.IOException;
import java.io.InputStream;

/**
 * 阿里云 oss 存储
 */
public class OSSStorage implements Storage {

    // 默认分块大小 100KB
    private static final long PART_SIZE = 1024 * 100L;

    // 上传默认任务数
    private static final int TASK_NUM = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 根据认证参数实例化oss存储
     */
    public OSSStorage() {

    }

    @Override
    public String upload(String filePath, String container, String key) {
        return null;
    }

    @Override
    public String upload(InputStream inputStream, String container, String key) {
        return null;
    }

    @Override
    public void download(String filePath, String container, String key) {

    }

    @Override
    public InputStream download(String container, String key) {
        return null;
    }

    @Override
    public void close() {
    }

}
