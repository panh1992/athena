package org.athena.plugin.jdbi;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class InstantMapper implements ColumnMapper<Instant> {

    @Override
    public Instant map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {

        Timestamp ts = r.getTimestamp(columnNumber);
        return ts == null ? null : Instant.ofEpochMilli(ts.getTime());

    }

}