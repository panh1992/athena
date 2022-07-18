package org.athena.account.business;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.athena.account.db.UserRepository;
import org.athena.account.entity.User;
import org.athena.account.resp.UserResp;
import org.athena.auth.jwt.JWTUtil;
import org.athena.common.exception.EntityAlreadyExistsException;
import org.athena.common.exception.EntityNotExistException;
import org.athena.common.exception.InternalServerError;
import org.athena.common.exception.InvalidParameterException;
import org.athena.common.util.Constant;
import org.athena.common.util.SnowflakeIdWorker;
import org.athena.common.util.crypto.BCryptUtil;
import org.athena.storage.business.StoreSpacesBusiness;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import java.time.Instant;
import java.util.Optional;

@Singleton
public class UserBusiness {

    private static final Logger logger = LoggerFactory.getLogger(UserBusiness.class);

    @Inject
    private SnowflakeIdWorker idWorker;

    @Inject
    private StoreSpacesBusiness storeSpacesBusiness;

    @Inject
    private UserRepository userRepository;

    /**
     * 注册用户
     *
     * @param userName 用户名
     * @param password 密码
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void register(String userName, String password) {
        Optional<User> checkUser = userRepository.findByUserName(userName);
        if (checkUser.isPresent()) {
            throw EntityAlreadyExistsException.build("用户名已存在");
        }
        User user = User.builder().userId(idWorker.nextId()).userName(userName)
                .passWord(BCryptUtil.hashPassWord(password)).createTime(Instant.now()).build();
        userRepository.save(user);
        storeSpacesBusiness.create(user.getUserId(), userName, null);
    }

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param passWord 密码
     * @return jwt Token
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public String login(String userName, String passWord) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty() || !BCryptUtil.checkPassWord(passWord, userOptional.get().getPassWord())) {
            throw InvalidParameterException.build("用户名或密码错误");
        }
        try {
            return JWTUtil.createToken(userOptional.get().getUserId().toString(), Constant.AUTHORIZATION_DURATION);
        } catch (JoseException e) {
            logger.error("JWT WEB TOKEN 创建失败, 异常信息: ", e);
            throw InternalServerError.build("JWT TOKEN 创建失败");
        }
    }

    /**
     * 获取用户信息
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public UserResp get(Long userId) {

        User user = this.getUser(userId);

        return UserResp.builder().userId(userId).userName(user.getUserName()).nickName(user.getNickName())
                .mobile(user.getMobile()).email(user.getEmail()).profile(user.getProfile())
                .createTime(user.getCreateTime()).build();
    }

    /**
     * 获取用户信息
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public User getUser(Long userId) {
        Optional<User> optional = userRepository.findByUserId(userId);
        if (optional.isEmpty()) {
            throw EntityNotExistException.build("此用户未找到, 请重试");
        }
        return optional.get();
    }

    /**
     * 修改用户信息
     */
    @InTransaction(TransactionIsolationLevel.REPEATABLE_READ)
    public void update(Long userId, String nickName, String email, String mobile, String profile) {
        Optional<User> optional = userRepository.findByUserId(userId);
        if (optional.isEmpty()) {
            throw EntityNotExistException.build("此用户未找到, 请重试");
        }
        User user = optional.get();
        user.setNickName(nickName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setProfile(profile);
        userRepository.update(user);
    }

    /**
     * 判断用户是否有此角色
     */
    @InTransaction(value = TransactionIsolationLevel.REPEATABLE_READ, readOnly = true)
    public boolean hasRole(Long userId, String role) {

        return userRepository.hasRole(userId, role);

    }

}
