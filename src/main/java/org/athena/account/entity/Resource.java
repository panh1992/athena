package org.athena.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "auth", name = "resource")
public class Resource {

    /**
     * 资源主键
     */
    @Id
    @Column(name = "resource_id")
    private Long resourceId;

    /**
     * 请求 URI
     */
    @Column(name = "uri")
    private String uri;

    /**
     * 请求 Method
     */
    @Column(name = "method")
    private String method;

    /**
     * 资源名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 所属模块
     */
    @Column(name = "module")
    private String module;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Instant createTime;

    /**
     * 更新时间
     */
    @Column(name = "modify_time")
    private Instant modifyTime;

    /**
     * 资源标识
     */
    @Column(name = "permission")
    private String permission;

    /**
     * 权限描述
     */
    @Column(name = "description")
    private String description;

}
