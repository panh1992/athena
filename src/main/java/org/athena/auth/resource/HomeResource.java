package org.athena.auth.resource;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.athena.auth.business.UserBusiness;
import org.athena.auth.params.LoginParams;
import org.athena.auth.params.RegisterParams;
import org.athena.auth.resp.UserResp;
import org.athena.common.resp.Response;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Api(tags = "A")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HomeResource {

    private UserBusiness userBusiness;

    public HomeResource(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    /**
     * 用户注册
     */
    @Timed
    @POST
    @Path("register")
    @ApiOperation(value = "注册", notes = "用户使用 用户名、邮箱、密码 注册")
    @ApiResponses({
            @ApiResponse(code = 200, message = "注册成功")
    })
    public Response register(@Valid RegisterParams params) {
        userBusiness.register(params.getUserName(), params.getPassWord());
        return Response.build();
    }

    /**
     * 用户登录
     */
    @Timed
    @POST
    @Path("login")
    @ApiOperation(value = "登录", notes = "用户使用 用户名或邮箱、密码 进行登录操作")
    @ApiResponses({
            @ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 404, message = "用户名或密码错误")
    })
    public Response<String> login(@Valid LoginParams params) {
        return Response.build(userBusiness.login(params.getUserName(), params.getPassWord()), "登录成功");
    }

    @Timed
    @GET
    @Path("list")
    public Response<List<UserResp>> findAll() {
        return Response.build(userBusiness.findAll());
    }

}