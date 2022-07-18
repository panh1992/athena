package org.athena.account.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Singleton;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;
import org.athena.account.business.UserBusiness;
import org.athena.account.params.UserInfoParams;
import org.athena.account.resp.UserResp;
import org.athena.auth.UserInfo;
import org.athena.common.resp.Result;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Timed
@Singleton
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "U 用户资源")
public class UserResource {

    @Inject
    private UserBusiness userBusiness;

    @ApiOperation(value = "获取用户信息", notes = "获取用户详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取用户信息成功", response = UserResp.class),
            @ApiResponse(code = 404, message = "不存在此用户")
    })
    @GET
    public Response get(@ApiParam(value = "用户主键", required = true) @PathParam("user_id") Long userId) {
        return Response.ok(Result.build(userBusiness.get(userId))).build();
    }

    /**
     * 修改用户信息
     */
    @ApiOperation(value = "修改用户信息", notes = "修改用户详情信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改用户信息成功")
    })
    @PUT
    public Response update(@Auth UserInfo userInfo,
                           @ApiParam(value = "参数", required = true) @Valid UserInfoParams params) {
        userBusiness.update(userInfo.getUserId(), params.getNickName(), params.getEmail(),
                params.getMobile(), params.getProfile());
        return Response.ok(Result.build()).build();
    }

}
