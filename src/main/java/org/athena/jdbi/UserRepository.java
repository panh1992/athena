package org.athena.jdbi;

import org.athena.entity.User;
import org.athena.entity.mapper.UserMapper;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.io.IOException;
import java.util.List;

public interface UserRepository {

    @SqlQuery("SELECT username FROM users")
    List<String> findName();

    @SqlUpdate("UPDATE users SET username = 'asdfg'")
    void updateUser();

    @SqlQuery("SELECT id, username, password FROM users")
    @RegisterRowMapper(UserMapper.class)
    List<User> findAll();

    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    default void testUser() throws IOException {

        updateUser();

        System.out.println(findName());

        throw new IOException("woof");
    }

}
