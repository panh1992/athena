package org.athena.account.db;

import org.athena.account.entity.Role;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;

import java.util.List;
import java.util.Optional;

@JdbiRepository
public interface RoleRepository {

    @SqlQuery("SELECT r.role_id, name, create_time, modify_time, description FROM auth.role AS r LEFT JOIN "
            + "auth.role_resource_relation AS rrr ON r.role_id = rrr.role_id WHERE rrr.resource_id = :resourceId")
    List<Role> findByResourceId(@Bind("resourceId") Long resourceId);

    @SqlQuery("SELECT * FROM auth.role WHERE role_id = :roleId")
    Optional<Role> findByRoleId(@Bind("roleId") Long roleId);

    @SqlQuery("SELECT * FROM auth.role WHERE name = :name")
    Optional<Role> findByName(@Bind("name") String name);

    @SqlUpdate("INSERT INTO auth.role (role_id, name, create_time, modify_time, description) VALUES (:roleId, "
            + ":name, :createTime, :modifyTime, :description)")
    void save(@BindBean Role role);

    @SqlUpdate("UPDATE auth.role SET name = :name, create_time = :createTime, modify_time = :modifyTime, "
            + "description = :description WHERE role_id = :roleId")
    void update(@BindBean Role role);

    @SqlUpdate("DELETE FROM auth.role WHERE role_id = :roleId")
    void delete(@Bind("roleId") Long roleId);

}
