package org.athena.account.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "资源信息参数")
public class ResourceParams {

    @ApiModelProperty("资源URI")
    private String uri;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("所属模块")
    private String module;

    @ApiModelProperty("资源描述")
    private String description;

}
