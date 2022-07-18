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
 * 文件存储后端
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store_backend")
public class StoreBackend {

    /**
     * 存储后端主键
     */
    @Id
    @Column(name = "store_backend_id")
    private Long storeBackendId;

    /**
     * 存储后端名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 存储协议
     */
    @Column(name = "protocol")
    private String protocol;

    /**
     * 存储容器（根目录）
     */
    @Column(name = "container")
    private String container;

    /**
     * 服务端点
     */
    @Column(name = "endpoint")
    private String endpoint;

    /**
     * 服务端口
     */
    @Column(name = "port")
    private Integer port;

    /**
     * 存储级别
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 是否激活
     */
    @Column(name = "is_active")
    private Integer active;

    /**
     * 存储后端的访问认证方式  empty   userPassword    accessIdKey accessToken
     */
    @Column(name = "auth_type")
    private Integer authType;

    /**
     * 认证参数1
     */
    @Column(name = "auth_params_1")
    private String authParams1;

    /**
     * 认证参数2
     */
    @Column(name = "auth_params_2")
    private String authParams2;

    /**
     * 认证参数3
     */
    @Column(name = "auth_params_3")
    private String authParams3;

    /**
     * 认证参数4
     */
    @Column(name = "auth_params_4")
    private String authParams4;

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
