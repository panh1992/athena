package org.athena.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

/**
 * 多级存储
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "multipolar_store")
public class MultipolarStore {

    /**
     * 多级存储主键
     */
    @Id
    @Column(name = "multipolar_store_id")
    private Long multipolarStoreId;

    /**
     * 文件
     */
    @Column(name = "file_id")
    private String fileId;

    /**
     * 实际存储在所属层级的状态  uploading, available, deleted
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 存储级别
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 是否是当前激活层级
     */
    @Column(name = "is_active")
    private Boolean active;
    /**
     * 实际存储路径
     */
    @Column(name = "store_path")
    private String storePath;

    /**
     * 存储文件大小
     */
    @Column(name = "store_size")
    private Long storeSize;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Instant createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Instant modifyTime;

}
