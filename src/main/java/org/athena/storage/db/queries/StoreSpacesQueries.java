package org.athena.storage.db.queries;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.athena.common.util.QueryUtil;
import org.athena.storage.entity.StoreSpace;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;

@Singleton
public class StoreSpacesQueries {

    @Inject
    private Jdbi jdbi;

    /**
     * 根据 用户id、名称 查询存储空间
     */
    public List<StoreSpace> findByCreatorIdAndNameLike(Long userId, String name, Long limit, Long offset) {
        return jdbi.withHandle(handle -> {
            Map<String, Object> params = Maps.newHashMap();
            params.put("creator_id", userId);
            params.put("limit", limit);
            params.put("offset", offset);
            String nameQuery = "";
            if (!Strings.isNullOrEmpty(name)) {
                nameQuery = " AND name LIKE :name ";
                params.put("name", QueryUtil.like(name));
            }
            String sql = String.join(nameQuery, "SELECT * FROM store_space WHERE creator_id = :creator_id ",
                    " limit :limit offset :offset");
            return handle.createQuery(sql).bindMap(params).mapTo(StoreSpace.class).list();
        });
    }

    /**
     * 根据 用户id、名称 查询存储空间
     */
    public Long countByCreatorIdAndNameLike(Long userId, String name) {
        return jdbi.withHandle(handle -> {
            Map<String, Object> params = Maps.newHashMap();
            params.put("creator_id", userId);
            String nameQuery = "";
            if (!Strings.isNullOrEmpty(name)) {
                nameQuery = " AND name LIKE :name ";
                params.put("name", QueryUtil.like(name));
            }
            String sql = String.join("", "SELECT COUNT(1) FROM store_space WHERE creator_id = ",
                    ":creator_id ", nameQuery);
            return handle.createQuery(sql).bindMap(params).mapTo(Long.class).one();
        });
    }

}
