package org.athena.account.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Singleton;
import io.swagger.annotations.*;
import org.athena.account.business.ResourceBusiness;
import org.athena.account.params.ResourceParams;
import org.athena.account.resp.ResourceResp;
import org.athena.common.resp.Result;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Timed
@Singleton
@Path("/resources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "R 权限资源")
public class ResourceResource {

    @Inject
    private ResourceBusiness resourceBusiness;

    @ApiOperation(value = "获取资源列表", notes = "获取资源列表")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取资源列表成功", response = ResourceResp.class)
    })
    @GET
    public Response findAll(@ApiParam(value = "资源名称") @QueryParam("name") String name,
                            @ApiParam(value = "所属平台") @QueryParam("platform") String platform,
                            @ApiParam(value = "所属模块") @QueryParam("module") String module,
                            @ApiParam(value = "资源URI") @QueryParam("uri") String uri,
                            @ApiParam(value = "请求方式") @QueryParam("method") String method,
                            @ApiParam(value = "限制几条记录", required = true) @QueryParam("limit") Long limit,
                            @ApiParam(value = "从第几条记录开始", required = true) @QueryParam("offset") Long offset) {
        return Response.ok(Result.build(resourceBusiness.findAll(name, platform, module, uri, method,
                limit, offset))).build();
    }

    @ApiOperation(value = "新增资源", notes = "新增资源信息")
    @ApiResponses({
            @ApiResponse(code = 201, message = "新增成功")
    })
    @POST
    public Response create(@ApiParam(value = "参数", required = true) @Valid ResourceParams params) {
        resourceBusiness.create(params);
        return Response.status(Response.Status.CREATED).entity(Result.build()).build();
    }

    @ApiOperation(value = "获取资源详情", notes = "获取资源详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取资源信息成功", response = ResourceResp.class)
    })
    @GET
    @Path("/{resource_id}")
    public Response get(@ApiParam(value = "资源主键", required = true) @PathParam("resource_id") Long resourceId) {
        return Response.ok(Result.build(resourceBusiness.get(resourceId))).build();
    }

    @ApiOperation(value = "修改资源", notes = "修改资源信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改成功")
    })
    @PUT
    @Path("/{resource_id}")
    public Response update(@ApiParam(value = "资源主键", required = true) @PathParam("resource_id") Long resourceId,
                           @ApiParam(value = "参数", required = true) @Valid ResourceParams params) {
        resourceBusiness.update(resourceId, params);
        return Response.ok(Result.build()).build();
    }

    @ApiOperation(value = "删除资源", notes = "删除资源信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @DELETE
    @Path("/{resource_id}")
    public Response delete(@ApiParam(value = "角色主键", required = true) @PathParam("resource_id") Long resourceId) {
        resourceBusiness.delete(resourceId);
        return Response.ok(Result.build()).build();
    }

}
