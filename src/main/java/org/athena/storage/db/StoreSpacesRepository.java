package org.athena.storage.db;

import org.athena.storage.entity.StoreSpace;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;

import java.util.Optional;

@JdbiRepository
public interface StoreSpacesRepository {

    @SqlQuery("SELECT * FROM store_space WHERE store_space_id = :store_space_id")
    Optional<StoreSpace> findByStoreSpaceId(@Bind("store_space_id") Long storeId);

    @SqlQuery("SELECT * FROM store_space WHERE name = :name")
    Optional<StoreSpace> findByName(@Bind("name") String name);

    @SqlQuery("SELECT * FROM store_space WHERE store_space_id = :store_space_id AND creator_id = :creator_id")
    Optional<StoreSpace> findByStoreSpaceIdAndCreatorId(@Bind("store_space_id") Long storeSpaceId,
                                                        @Bind("creator_id") Long userId);

    @SqlUpdate("INSERT INTO store_space (store_space_id, creator_id, name, size, is_deleted, create_time, "
            + "modify_time, description) VALUES (:storeSpaceId, :creatorId, :name, :size, :deleted, :createTime,"
            + " :modifyTime, :description)")
    void save(@BindBean StoreSpace storeSpace);

    @SqlUpdate("UPDATE store_space SET creator_id = :creatorId, name = :name, size = :size,"
            + " is_deleted = :deleted, create_time = :createTime, modify_time = :modifyTime,"
            + " description = :description WHERE store_space_id = :storeSpaceId")
    void update(@BindBean StoreSpace storeSpace);

}
