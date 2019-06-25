package org.athena.storage.db;

import org.athena.storage.entity.AthenaFile;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface FileRepository {

    @SqlQuery("SELECT * FROM files")
    List<AthenaFile> findAll();

}