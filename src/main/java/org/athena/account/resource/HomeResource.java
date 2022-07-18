package org.athena.account.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Singleton;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.athena.account.business.UserBusiness;
import org.athena.account.params.LoginRegisterParams;
import org.athena.common.resp.Result;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Timed
@Singleton
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "A 通用资源")
public class HomeResource {

    @Inject
    private UserBusiness userBusiness;

    /**
     * 用户注册
     */
    @POST
    @Path("register")
    @ApiOperation(value = "注册", notes = "用户使用 用户名、邮箱、密码 注册")
    @ApiResponses({
            @ApiResponse(code = 200, message = "注册成功")
    })
    public Result<Void> register(@Valid LoginRegisterParams params) {
        userBusiness.register(params.getUserName(), params.getPassWord());
        return Result.build();
    }

    /**
     * 用户登录
     */
    @POST
    @Path("login")
    @ApiOperation(value = "登录", notes = "用户使用 用户名或邮箱、密码 进行登录操作")
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功", response = String.class),
            @ApiResponse(code = 404, message = "用户名或密码错误")
    })
    public Result<String> login(@Valid LoginRegisterParams params) {
        return Result.build(userBusiness.login(params.getUserName(), params.getPassWord()), "登录成功");
    }

}
