package org.athena.account.db;

import org.athena.account.entity.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;

import java.util.Optional;

@JdbiRepository
public interface UserRepository {

    @SqlQuery("SELECT * FROM auth.user WHERE username = :username")
    Optional<User> findByUserName(@Bind("username") String username);

    @SqlQuery("SELECT * FROM auth.user WHERE user_id = :userId")
    Optional<User> findByUserId(@Bind("userId") Long userId);

    @SqlUpdate("INSERT INTO auth.user (user_id, username, nickname, password, email, mobile, profile, create_time) "
            + "VALUES (:userId, :userName, :nickName, :passWord, :email, :mobile, :profile, :createTime)")
    void save(@BindBean User user);

    @SqlUpdate("UPDATE auth.user SET username = :userName, nickname = :nickName, password = :passWord, email = :email,"
            + " mobile = :mobile, profile = :profile, create_time = :createTime WHERE user_id = :userId")
    void update(@BindBean User user);

    @SqlQuery("SELECT COUNT(1) > 0 FROM auth.user AS u LEFT JOIN auth.user_role_relation AS urr ON u.user_id = "
            + "urr.user_id LEFT JOIN auth.role AS r ON urr.role_id = r.role_id WHERE u.user_id = :userId "
            + "AND r.name = :role")
    boolean hasRole(@Bind("userId") Long userId, @Bind("role") String role);

}
