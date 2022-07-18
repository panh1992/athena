package org.athena.storage.params;

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
@ApiModel(description = "修改 存储空间参数")
public class SpaceUpdateParams {

    @ApiModelProperty("描述信息")
    private String description;

}
