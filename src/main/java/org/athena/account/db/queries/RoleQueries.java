package org.athena.account.db.queries;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.athena.account.entity.Role;
import org.athena.common.util.QueryUtil;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;

@Singleton
public class RoleQueries {

    @Inject
    private Jdbi jdbi;

    /**
     * 查询角色列表
     */
    public List<Role> findAll(String name, Long limit, Long offset) {
        return jdbi.withHandle(handle -> {
            Map<String, Object> params = Maps.newHashMap();
            params.put("limit", limit);
            params.put("offset", offset);
            String nameQuery = "";
            if (!Strings.isNullOrEmpty(name)) {
                nameQuery = " WHERE name LIKE :name ";
                params.put("name", QueryUtil.like(name));
            }
            String sql = String.join(nameQuery, "SELECT * FROM auth.role ", "LIMIT :limit OFFSET :offset");
            return handle.createQuery(sql).bindMap(params).mapTo(Role.class).list();
        });
    }

    /**
     * 统计角色列表
     */
    public Long countAll(String name) {
        return jdbi.withHandle(handle -> {
            Map<String, Object> params = Maps.newHashMap();
            String nameQuery = "";
            if (!Strings.isNullOrEmpty(name)) {
                nameQuery = " WHERE name LIKE :name ";
                params.put("name", QueryUtil.like(name));
            }
            String sql = "SELECT COUNT(1) FROM auth.role ".concat(nameQuery);
            return handle.createQuery(sql).bindMap(params).mapTo(Long.class).one();
        });
    }

}
