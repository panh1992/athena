package org.athena.plugin.jdbi;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;

public class InstantPlugin implements JdbiPlugin {

    @Override
    public void customizeJdbi(Jdbi db) {

        db.registerArgument(new InstantArgumentFactory());
        db.registerColumnMapper(new InstantMapper());

    }

}
