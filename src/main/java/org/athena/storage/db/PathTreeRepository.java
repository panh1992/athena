package org.athena.storage.db;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;

@JdbiRepository
public interface PathTreeRepository {

    @SqlUpdate("INSERT INTO path_tree(ancestor_id, descendant_id, depth) SELECT t.ancestor_id, :descendantId, "
            + "t.depth + 1 FROM path_tree AS t WHERE t.descendant_id = :ancestorId "
            + "UNION ALL SELECT :descendantId, :descendantId, 0")
    void save(@Bind("ancestorId") Long ancestorId, @Bind("descendantId") Long descendantId);

}
