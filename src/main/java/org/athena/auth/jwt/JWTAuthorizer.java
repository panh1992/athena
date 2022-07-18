package org.athena.auth.jwt;

import io.dropwizard.auth.Authorizer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.athena.account.business.UserBusiness;
import org.athena.auth.UserInfo;

/**
 * 完成 用户权限 校验
 */
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthorizer implements Authorizer<UserInfo> {

    private UserBusiness userBusiness;

    @Override
    public boolean authorize(UserInfo principal, String role) {
        return userBusiness.hasRole(principal.getUserId(), role);
    }

}
