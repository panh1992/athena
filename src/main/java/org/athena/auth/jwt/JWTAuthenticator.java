package org.athena.auth.jwt;

import io.dropwizard.auth.Authenticator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.athena.account.business.UserBusiness;
import org.athena.account.entity.User;
import org.athena.auth.UserInfo;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;

import java.util.Optional;

/**
 * 完成由 JWT token 转换成 用户操作
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthenticator implements Authenticator<JwtClaims, UserInfo> {

    private UserBusiness userBusiness;

    @Override
    public Optional<UserInfo> authenticate(JwtClaims credentials) {
        try {
            User user = userBusiness.getUser(Long.parseLong(credentials.getSubject()));
            return Optional.of(UserInfo.builder().userId(user.getUserId()).name(user.getUserName()).build());
        } catch (MalformedClaimException e) {
            log.error("转换用户信息失败 ", e);
        }
        return Optional.empty();
    }

}
