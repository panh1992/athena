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
 * 上传任务
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "upload_task")
public class UploadTask {

    /**
     * 上传任务主键
     */
    @Id
    @Column(name = "upload_task_id")
    private Long uploadTaskId;

    /**
     * 存储空间
     */
    @Column(name = "store_space_id")
    private String storeSpaceId;

    /**
     * 创建用户
     */
    @Column(name = "creator_id")
    private String creatorId;

    /**
     * 数据上传任务的状态   start   success  failed
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Boolean deleted;

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
     * 修改时间
     */
    @Column(name = "finish_time")
    private Instant finishTime;

}
