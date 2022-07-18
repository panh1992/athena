package org.athena.storage.resp;

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
@ApiModel(description = "存储空间响应实体")
public class StoreSpaceResp {

    @ApiModelProperty("存储空间主键")
    private Long storeSpacesId;

    @ApiModelProperty("创建用户主键")
    private Long creatorId;

    @ApiModelProperty("存储空间名称")
    private String name;

    @ApiModelProperty("存储空间大小")
    private Long size;

    @ApiModelProperty("是否删除")
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    private Instant createTime;

    @ApiModelProperty("描述信息")
    private String description;

}
