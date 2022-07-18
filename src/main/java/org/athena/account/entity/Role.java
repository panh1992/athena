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
@Table(schema = "auth", name = "role")
public class Role {

    /**
     * 角色主键
     */
    @Id
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

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
     * 角色描述
     */
    @Column(name = "description")
    private String description;

}
