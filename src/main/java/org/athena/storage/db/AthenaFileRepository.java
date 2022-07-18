package org.athena.storage.db;

import org.athena.storage.entity.AthenaFile;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;

import java.util.List;
import java.util.Optional;

@JdbiRepository
public interface AthenaFileRepository {

    @SqlQuery("SELECT descendant.* FROM athena_file AS ancestor INNER JOIN path_tree ON ancestor.file_id = ancestor_id"
            + " INNER JOIN athena_file AS descendant ON descendant_id = descendant.file_id WHERE ancestor.file_id = "
            + ":fileId AND path_tree.depth = :depth limit :limit offset :offset")
    List<AthenaFile> findByDescendantFileAndDepth(@Bind("fileId") Long fileId, @Bind("depth") Long depth,
                                                  @Bind("limit") Long limit, @Bind("offset") Long offset);

    @SqlQuery("SELECT count(1) FROM athena_file AS ancestor INNER JOIN path_tree ON ancestor.file_id = ancestor_id"
            + " INNER JOIN athena_file AS descendant ON descendant_id = descendant.file_id WHERE "
            + "ancestor.file_id = :fileId AND path_tree.depth = :depth")
    Long countByDescendantFileAndDepth(@Bind("fileId") Long fileId, @Bind("depth") Long depth);

    @SqlQuery("SELECT ancestor.* FROM athena_file AS ancestor INNER JOIN path_tree ON ancestor.file_id = "
            + "ancestor_id INNER JOIN athena_file AS descendant ON descendant_id = descendant.file_id WHERE "
            + "ancestor.store_space_id = :storeSpaceId AND ancestor.file_name = :name AND depth = :depth")
    Optional<AthenaFile> findByStoreSpaceIdAndFileNameAndDepth(
            @Bind("storeSpaceId") Long storeSpaceId, @Bind("name") String name, @Bind("depth") int depth);

    @SqlQuery("SELECT descendant.* FROM athena_file AS ancestor INNER JOIN path_tree ON ancestor.file_id = ancestor_id "
            + "INNER JOIN athena_file AS descendant ON descendant_id = descendant.file_id WHERE "
            + "ancestor.store_space_id = :storeSpaceId and descendant.file_name = :name AND depth = :depth")
    Optional<AthenaFile> findByStoreSpaceIdAndDescendantFileNameAndDepth(
            @Bind("storeSpaceId") Long storeSpaceId, @Bind("name") String name, @Bind("depth") int depth);

    @SqlQuery("SELECT '/'||string_agg(ancestor.file_name, '/') as filePath FROM athena_file AS ancestor INNER JOIN "
            + "path_tree ON ancestor.file_id = ancestor_id INNER JOIN athena_file AS descendant ON descendant_id = "
            + "descendant.file_id WHERE descendant.store_space_id = :storeSpaceId AND descendant.file_id = :fileId "
            + "GROUP BY ancestor.store_space_id")
    String findFilePath(@Bind("storeSpaceId") Long storeSpaceId, @Bind("fileId") Long fileId);

    @SqlQuery("SELECT * FROM athena_file WHERE file_id = :fileId AND creator_id = :userId")
    Optional<AthenaFile> findByCreatorIdAndFileId(@Bind("userId") Long userId, @Bind("fileId") Long fileId);

    @SqlUpdate("UPDATE athena_file SET status = :status FROM path_tree WHERE file_id = descendant_id and ancestor_id ="
            + " :fileId and creator_id = :creator_id")
    void removeAthenaFile(@Bind("creator_id") Long creatorId, @Bind("fileId") Long fileId,
                          @Bind("status") String status);

    @SqlUpdate("INSERT INTO athena_file (file_id, store_space_id, creator_id, store_space, file_name, file_size, "
            + "source_id, source_type, is_dir, check_sum, format, status, create_time, modify_time, description) "
            + "VALUES (:fileId, :storeSpaceId, :creatorId, :storeSpaceName, :fileName, :fileSize, :sourceId, "
            + ":sourceType, :dir, :checkSum, :format, :status, :createTime, :modifyTime, :description)")
    void save(@BindBean AthenaFile file);

    @SqlUpdate("UPDATE athena_file SET store_space_id = :storeSpaceId, creator_id = :creatorId, store_space = "
            + ":storeSpaceName, file_name = :fileName, file_size = :fileSize, source_id = :sourceId, source_type = "
            + ":sourceType, is_dir = :dir, check_sum = :checkSum, format = :format, status = :status, create_time = "
            + ":createTime, modify_time = :modifyTime, description = :description WHERE file_id = :fileId")
    void update(@BindBean AthenaFile file);

}
