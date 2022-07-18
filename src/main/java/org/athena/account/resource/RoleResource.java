package org.athena.account.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Singleton;
import io.swagger.annotations.*;
import org.athena.account.business.RoleBusiness;
import org.athena.account.params.RoleParams;
import org.athena.account.resp.RoleResp;
import org.athena.common.resp.Result;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Timed
@Singleton
@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "R 角色资源")
public class RoleResource {

    @Inject
    private RoleBusiness roleBusiness;

    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取角色列表成功", response = RoleResp.class)
    })
    @GET
    public Response findAll(@ApiParam(value = "角色名称") @QueryParam("name") String name,
                                @ApiParam(value = "限制几条记录", required = true) @QueryParam("limit") Long limit,
                                @ApiParam(value = "从第几条记录开始", required = true) @QueryParam("offset") Long offset) {
        return Response.ok(Result.build(roleBusiness.findAll(name, limit, offset))).build();
    }

    @ApiOperation(value = "新增角色", notes = "新增角色信息")
    @ApiResponses({
            @ApiResponse(code = 201, message = "新增成功")
    })
    @POST
    public Response create(@ApiParam(value = "参数", required = true) @Valid RoleParams params) {
        roleBusiness.create(params.getName(), params.getDescription());
        return Response.status(Response.Status.CREATED).entity(Result.build()).build();
    }

    @ApiOperation(value = "获取角色详情", notes = "获取角色详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取角色信息成功", response = RoleResp.class)
    })
    @GET
    @Path("/{role_id}")
    public Response get(@ApiParam(value = "角色主键", required = true) @PathParam("role_id") Long roleId) {
        return Response.ok(Result.build(roleBusiness.get(roleId))).build();
    }

    @ApiOperation(value = "修改角色", notes = "修改角色信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改成功")
    })
    @PUT
    @Path("/{role_id}")
    public Response update(@ApiParam(value = "角色主键", required = true) @PathParam("role_id") Long roleId,
                               @ApiParam(value = "参数", required = true) @Valid RoleParams params) {
        roleBusiness.update(roleId, params.getName(), params.getDescription());
        return Response.ok(Result.build()).build();
    }

    @ApiOperation(value = "删除角色", notes = "删除角色信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @DELETE
    @Path("/{role_id}")
    public Response delete(@ApiParam(value = "角色主键", required = true) @PathParam("role_id") Long roleId) {
        roleBusiness.delete(roleId);
        return Response.ok(Result.build()).build();
    }

}
