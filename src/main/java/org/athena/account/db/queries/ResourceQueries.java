package org.athena.account.db.queries;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.athena.account.entity.Resource;
import org.athena.common.util.QueryUtil;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;

@Singleton
public class ResourceQueries {

    @Inject
    private Jdbi jdbi;

    /**
     * 查询角色列表
     */
    public List<Resource> findAll(String name, String platform, String module, String uri, String method,
                                  Long limit, Long offset) {
        return jdbi.withHandle(handle -> {
            Map<String, Object> params = Maps.newHashMap();
            params.put("limit", limit);
            params.put("offset", offset);
            String sql = "SELECT * FROM auth.resource "
                    .concat(this.setConditions(params, name, platform, module, uri, method))
                    .concat(" LIMIT :limit OFFSET :offset");
            return handle.createQuery(sql).bindMap(params).mapTo(Resource.class).list();
        });
    }

    /**
     * 统计角色列表
     */
    public Long countAll(String name, String platform, String module, String uri, String method) {
        return jdbi.withHandle(handle -> {
            Map<String, Object> params = Maps.newHashMap();
            String sql = "SELECT COUNT(1) FROM auth.resource "
                    .concat(this.setConditions(params, name, platform, module, uri, method));
            return handle.createQuery(sql).bindMap(params).mapTo(Long.class).one();
        });
    }

    /**
     * 设置查询条件
     */
    private String setConditions(Map<String, Object> params, String name, String platform, String module,
                               String uri, String method) {
        StringBuilder query = new StringBuilder();
        if (!Strings.isNullOrEmpty(name)) {
            query.append(" AND name LIKE :name ");
            params.put("name", QueryUtil.like(name));
        }
        if (!Strings.isNullOrEmpty(platform)) {
            query.append(" AND platform LIKE :platform ");
            params.put("platform", QueryUtil.like(platform));
        }
        if (!Strings.isNullOrEmpty(module)) {
            query.append(" AND module LIKE :module ");
            params.put("module", QueryUtil.like(module));
        }
        if (!Strings.isNullOrEmpty(uri)) {
            query.append(" AND uri LIKE :uri ");
            params.put("uri", QueryUtil.like(uri));
        }
        if (!Strings.isNullOrEmpty(method)) {
            query.append(" AND method = :method ");
            params.put("method", method);
        }
        return query.length() > 0 ? " WHERE ".concat(query.substring(4)) : "";
    }

}
