package org.athena.storage.sdk;

import java.io.InputStream;

/**
 * 定义存储工厂
 */
public interface Storage {

    /**
     * 将本地文件路径上传到目标存储上
     *
     * @param filePath  文件路径
     * @param container 存储空间
     * @param key       存储key
     * @return 存储平台的校验码
     */
    String upload(String filePath, String container, String key);

    /**
     * 将输入流上传到目标存储上
     *
     * @param inputStream 文件输入流
     * @param container   存储空间
     * @param key         存储key
     * @return 存储平台的校验码
     */
    String upload(InputStream inputStream, String container, String key);

    /**
     * 将目标存储上文件路径下载到本地路径
     *
     * @param filePath  本地路径
     * @param container 存储空间
     * @param key       存储key
     */
    void download(String filePath, String container, String key);

    /**
     * 获取目标存储上文件输入流
     *
     * @param container 存储空间
     * @param key       存储key
     * @return 目标存储上文件输入流
     */
    InputStream download(String container, String key);

    /**
     * 用于 流 上传 下载 后关闭连接
     */
    void close();

}
