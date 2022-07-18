package org.athena.account.db;

import org.athena.account.entity.Resource;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;

import java.util.List;
import java.util.Optional;

@JdbiRepository
public interface ResourceRepository {

    @SqlQuery("SELECT COUNT(1) > 0 FROM auth.resource WHERE uri = :uri AND method IN (<methods>)")
    boolean exists(@Bind("uri") String uri, @BindList("methods") List<String> methods);

    @SqlQuery("SELECT * FROM auth.resource WHERE uri = :uri AND method = :method")
    Optional<Resource> findByUriAndMethod(@Bind("uri") String uri, @Bind("method") String method);

    @SqlQuery("SELECT * FROM auth.resource WHERE resource_id = :resourceId")
    Optional<Resource> findByResourceId(@BindBean("resourceId") Long resourceId);

    @SqlUpdate("INSERT INTO auth.resource (resource_id, uri, method, name, platform, module, create_time, modify_time,"
            + " permission, description) VALUES (:resourceId, :uri, :method, :name, :platform, :module, :createTime,"
            + " :modifyTime, :permission, :description)")
    void save(@BindBean Resource resource);

    @SqlUpdate("UPDATE auth.resource SET uri = :uri, method = :method, name = :name, platform = :platform, module = "
            + ":module, create_time = :createTime, modify_time = :modifyTime, permission = :permission, description = "
            + ":description WHERE resource_id = :resourceId")
    void update(@BindBean Resource resource);

    @SqlUpdate("DELETE FROM auth.resource WHERE resource_id = :resourceId")
    void delete(@BindBean("resourceId") Long resourceId);

}
