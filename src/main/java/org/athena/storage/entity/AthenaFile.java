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
 * 用户视角文件元数据
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "athena_file")
public class AthenaFile {

    /**
     * 文件主键
     */
    @Id
    @Column(name = "file_id")
    private Long fileId;

    /**
     * 存储空间
     */
    @Column(name = "store_space_id")
    private Long storeSpaceId;

    /**
     * 创建用户
     */
    @Column(name = "creator_id")
    private Long creatorId;

    /**
     * 文件所属存储空间名称
     */
    @Column(name = "store_space_name")
    private String storeSpaceName;

    /**
     * 文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 用户角度文件大小   单位 byte
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * 存储来源
     */
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 存储来源类型  upload   mount   system    task
     */
    @Column(name = "source_type")
    private String sourceType;

    /**
     * 是否为目录
     */
    @Column(name = "is_dir")
    private Boolean dir;

    /**
     * 文件的校验码
     */
    @Column(name = "check_sum")
    private String checkSum;

    /**
     * 文件格式      以文件扩展名为依据
     */
    @Column(name = "format")
    private String format;

    /**
     * 文件状态   new   uploading   available   failed   deleted
     */
    @Column(name = "status")
    private String status;

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

    /**
     * 描述信息
     */
    @Column(name = "description")
    private String description;

}

