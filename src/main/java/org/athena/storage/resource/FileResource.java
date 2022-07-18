package org.athena.storage.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;
import org.athena.auth.UserInfo;
import org.athena.common.resp.Result;
import org.athena.common.util.QueryUtil;
import org.athena.storage.business.AthenaFileBusiness;
import org.athena.storage.params.CreateFileParams;
import org.athena.storage.resp.FileInfoResp;
import org.athena.storage.resp.FileResp;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

/**
 * 文件资源 处理文件元数据
 */
@Timed
@Singleton
@Path("/files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "F 文件服务")
public class FileResource {

    @Inject
    private AthenaFileBusiness athenaFileBusiness;

    @ApiOperation(value = "文件列表", notes = "获取 某存储空间，某文件夹 下级文件列表信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取文件列表成功", response = FileResp.class, responseContainer = "List")
    })
    @GET
    public Response findNextAll(@Auth UserInfo userInfo,
            @ApiParam(value = "存储空间主键, 不传取默认存储空间") @QueryParam("store_space_id") Long storeSpaceId,
            @ApiParam(value = "文件主键, 不传取存储空间根文件") @QueryParam("file_id") Long fileId,
            @ApiParam(value = "限制几条记录", required = true) @QueryParam("limit") Long limit,
            @ApiParam(value = "从第几条记录开始", required = true) @QueryParam("offset") Long offset) {
        return Response.ok(Result.build(athenaFileBusiness.findNextAll(userInfo.getUserId(), storeSpaceId, fileId,
                QueryUtil.limit(limit), QueryUtil.offset(offset)))).build();
    }

    @ApiOperation(value = "获取文件信息", notes = "获取文件详细信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取文件信息成功", response = FileInfoResp.class)
    })
    @GET
    @Path("/{file_id}")
    public Response get(@Auth UserInfo userInfo, @ApiParam("文件主键") @PathParam("file_id") Long fileId) {
        return Response.ok(Result.build(athenaFileBusiness.get(userInfo.getUserId(), fileId))).build();
    }

    /**
     * 新建文件元数据信息
     */
    @ApiOperation(value = "新建文件", notes = "新建文件元数据信息")
    @ApiResponses({
            @ApiResponse(code = 201, message = "新建成功")
    })
    @POST
    public Response create(@Auth UserInfo userInfo, @Valid CreateFileParams params) {
        athenaFileBusiness.create(userInfo.getUserId(), params.getStoreSpaceId(), params.getFilePath(),
                params.getIsDir(), params.getDescription());
        return Response.status(Response.Status.CREATED).entity(Result.build()).build();
    }

    /**
     * 移动文件
     */
    @ApiOperation(value = "移动文件", notes = "移动文件元数据信息")
    @ApiResponses({
            @ApiResponse(code = 201, message = "移动成功")
    })
    @PATCH
    @Path("/{file_id}")
    public Response move(@Auth UserInfo userInfo, @ApiParam("文件主键") @PathParam("file_id") Long fileId,
                         @ApiParam("文件主键") @QueryParam("file_id") Long fileDirId) {
        athenaFileBusiness.move(userInfo.getUserId(), fileId, fileDirId);
        return Response.ok(Result.build()).build();
    }

    /**
     * 删除文件
     */
    @ApiOperation(value = "删除文件", notes = "删除文件元数据信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 400, message = "根目录不允许删除"),
            @ApiResponse(code = 409, message = "删除失败, 该目录下存在文件")
    })
    @DELETE
    @Path("/{file_id}")
    public Response remove(@Auth UserInfo userInfo, @ApiParam("文件主键") @PathParam("file_id") Long fileId,
                           @ApiParam("是否强制删除") @PathParam("delete") Boolean delete) {
        athenaFileBusiness.remove(userInfo.getUserId(), fileId, Objects.isNull(delete) ? null : delete);
        return Response.ok(Result.build()).build();
    }

    @ApiOperation(value = "文件列表", notes = "获取文件列表信息")
    @POST
    @Path("/upload")
    public Result<Void> upload(final FormDataMultiPart multiPart) {
        return null;
    }

}
