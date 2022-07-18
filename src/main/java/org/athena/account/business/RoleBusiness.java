package org.athena.account.business;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.collections4.CollectionUtils;
import org.athena.account.db.RoleRepository;
import org.athena.account.db.queries.RoleQueries;
import org.athena.account.entity.Role;
import org.athena.account.resp.RoleResp;
import org.athena.common.exception.EntityAlreadyExistsException;
import org.athena.common.exception.EntityNotExistException;
import org.athena.common.resp.PageResp;
import org.athena.common.util.SnowflakeIdWorker;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class RoleBusiness {

    @Inject
    private SnowflakeIdWorker idWorker;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private RoleQueries roleQueries;

    /**
     * 获取角色列表
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public PageResp<RoleResp> findAll(String name, Long limit, Long offset) {
        List<Role> roles = roleQueries.findAll(name, limit, offset);
        if (CollectionUtils.isEmpty(roles)) {
            return PageResp.of(Lists.newArrayList(), limit, offset);
        }
        long total = roleQueries.countAll(name);
        return PageResp.of(roles.stream().map(this::build).collect(Collectors.toList()), limit, offset, total);
    }

    /**
     * 根据资源id 获取对应的角色信息
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public RoleResp get(Long roleId) {
        Role role = this.getRole(roleId);
        return this.build(role);
    }

    /**
     * 根据角色主键获取角色信息
     */
    private Role getRole(Long roleId) {
        Optional<Role> optional = roleRepository.findByRoleId(roleId);
        if (!optional.isPresent()) {
            throw EntityNotExistException.build("该角色信息不存在");
        }
        return optional.get();
    }

    /**
     * 构造角色返回实体
     */
    private RoleResp build(Role role) {
        return RoleResp.builder().roleId(role.getRoleId()).name(role.getName()).createTime(role.getCreateTime())
                .modifyTime(role.getModifyTime()).description(role.getDescription()).build();
    }

    /**
     * 根据资源id 获取对应的角色信息
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public List<Role> getByResourceId(Long resourceId) {
        return roleRepository.findByResourceId(resourceId);
    }

    /**
     * 创建角色信息
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void create(String name, String description) {
        Optional<Role> optional = roleRepository.findByName(name);
        if (optional.isPresent()) {
            throw EntityAlreadyExistsException.build("此角色已存在，请重试");
        }
        Role role = Role.builder().roleId(idWorker.nextId()).name(name).description(description)
                .createTime(Instant.now()).build();
        roleRepository.save(role);
    }

    /**
     * 修改角色信息
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void update(Long roleId, String name, String description) {
        Role role = this.getRole(roleId);
        Optional<Role> optional = roleRepository.findByName(name);
        if (optional.isPresent()) {
            throw EntityAlreadyExistsException.build("此角色已存在，请重试");
        }
        role.setName(name);
        role.setDescription(description);
        role.setModifyTime(Instant.now());
        roleRepository.update(role);
    }

    /**
     * 删除角色信息
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void delete(Long roleId) {
        Role role = this.getRole(roleId);
        roleRepository.delete(role.getRoleId());
    }

}
