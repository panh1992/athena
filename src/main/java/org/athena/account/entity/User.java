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

/**
 * 用户
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "auth", name = "user")
public class User {

    /**
     * 用户主键
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名称
     */
    @Column(name = "username")
    private String userName;

    /**
     * 用户昵称
     */
    @Column(name = "nickname")
    private String nickName;

    /**
     * 用户密码
     */
    @Column(name = "password")
    private String passWord;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 个人简介
     */
    @Column(name = "profile")
    private String profile;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Instant createTime;

}
