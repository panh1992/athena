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
@ApiModel(description = "角色列表响应实体")
public class RoleResp {

    @ApiModelProperty("角色主键")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("创建时间")
    private Instant createTime;

    @ApiModelProperty("更新时间")
    private Instant modifyTime;

    @ApiModelProperty("角色描述")
    private String description;

}
