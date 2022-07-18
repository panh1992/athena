package org.athena.storage.sdk;

import java.io.InputStream;

/**
 * 阿里云 oss 存储
 */
public class OSSStorage implements Storage {

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
