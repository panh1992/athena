package org.athena.db;

import org.athena.api.User;
import org.athena.exceptions.EntityAlreadyExists;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface UserRepository {

    @SqlUpdate("INSERT INTO users (id, user_name, nick_name, password, email, mobile, profile, create_time) VALUES "
            + "(:id, :userName, :nickName, :passWord, :email, :mobile, :profile, :createTime)")
    void save(@BindBean User user);

    @SqlUpdate("UPDATE users SET username = 'asdfg'")
    void updateUser();

    @SqlQuery("SELECT id, user_name, nick_name, password, email, mobile, profile, create_time FROM users")
    List<User> findAll();

    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    default void testUser() {

        updateUser();

        throw EntityAlreadyExists.build("woof");
    }

}
