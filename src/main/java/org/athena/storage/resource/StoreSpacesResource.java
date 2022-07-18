package org.athena.storage.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;
import org.athena.auth.UserInfo;
import org.athena.common.resp.Result;
import org.athena.common.util.QueryUtil;
import org.athena.storage.business.StoreSpacesBusiness;
import org.athena.storage.params.SpaceParams;
import org.athena.storage.params.SpaceUpdateParams;
import org.athena.storage.resp.StoreSpaceResp;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 文件资源 处理文件元数据
 */
@Timed
@Singleton
@Path("/spaces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "S 存储空间")
public class StoreSpacesResource {

    @Inject
    private StoreSpacesBusiness storeSpacesBusiness;

    @ApiOperation(value = "新建存储空间", notes = "新建存储空间")
    @ApiResponses({
            @ApiResponse(code = 201, message = "新建成功")
    })
    @POST
    public Response create(@Auth UserInfo userInfo,
                           @ApiParam(value = "参数", required = true) @Valid SpaceParams params) {
        storeSpacesBusiness.create(userInfo.getUserId(), params.getName(), params.getDescription());
        return Response.status(Response.Status.CREATED).entity(Result.build()).build();
    }

    @ApiOperation(value = "查询存储空间", notes = "查询存储空间")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取存储空间成功", response = StoreSpaceResp.class)
    })
    @GET
    public Response find(@Auth UserInfo userInfo,
                         @ApiParam(value = "存储空间名称") @QueryParam("name") String name,
                         @ApiParam(value = "限制几条记录", required = true) @QueryParam("limit") Long limit,
                         @ApiParam(value = "从第几条记录开始", required = true) @QueryParam("offset") Long offset) {
        return Response.ok(storeSpacesBusiness.find(userInfo.getUserId(), name, QueryUtil.limit(limit),
                QueryUtil.offset(offset))).build();
    }

    @ApiOperation(value = "查询存储空间详情", notes = "查询存储空间详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取存储空间详情成功", response = StoreSpaceResp.class)
    })
    @GET
    @Path("/{store_space_id}")
    public Response get(@Auth UserInfo userInfo,
                        @ApiParam(value = "存储空间主键", required = true) @PathParam("store_space_id") Long storeSpaceId) {
        return Response.ok(storeSpacesBusiness.get(userInfo.getUserId(), storeSpaceId)).build();
    }

    @ApiOperation(value = "修改存储空间备注", notes = "修改存储空间备注")
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改成功")
    })
    @PATCH
    @Path("/{store_space_id}")
    public Response update(@Auth UserInfo userInfo,
                           @ApiParam(value = "存储空间主键", required = true) @PathParam("store_space_id") Long storeSpaceId,
                           @ApiParam(value = "参数", required = true) @Valid SpaceUpdateParams params) {
        storeSpacesBusiness.update(userInfo.getUserId(), storeSpaceId, params.getDescription());
        return Response.ok().entity(Result.build()).build();
    }

}
