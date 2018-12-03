package org.athena.utils.plugin;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.joda.time.Instant;

import java.sql.Timestamp;
import java.sql.Types;

public class InstantArgumentFactory extends AbstractArgumentFactory<Instant> {

    public InstantArgumentFactory() {

        super(Types.TIMESTAMP);

    }

    @Override
    protected Argument build(Instant value, ConfigRegistry config) {

        return (pos, stmt, ctx) -> stmt.setTimestamp(pos, new Timestamp(value.getMillis()));

    }

}
