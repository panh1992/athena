package org.athena.account.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "资源列表响应实体")
public class ResourceResp {

    @ApiModelProperty("资源主键")
    private Long resourceId;

    @ApiModelProperty("资源URI")
    private String uri;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("所属模块")
    private String module;

    @ApiModelProperty("创建时间")
    private Instant createTime;

    @ApiModelProperty("更新时间")
    private Instant modifyTime;

    @ApiModelProperty("资源标识")
    private String permission;

    @ApiModelProperty("权限描述")
    private String description;

}
