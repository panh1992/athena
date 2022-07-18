package org.athena.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 文件树形关系 （采用闭包表）
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "path_tree")
public class PathTree {

    /**
     * 祖先文件
     */
    @Column(name = "ancestor_id")
    private String ancestorId;

    /**
     * 子孙文件
     */
    @Column(name = "descendant_id")
    private String descendantId;

    /**
     * 相对层级
     */
    @Column(name = "depth")
    private Integer depth;

}
