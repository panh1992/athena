package org.athena.storage.params;

import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "新建文件元数据参数")
public class CreateFileParams {

    @ApiModelProperty("存储空间ID")
    private Long storeSpaceId;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("是否为目录")
    private Boolean isDir;

    @ApiModelProperty("描述信息")
    private String description;

    @JsonSetter
    public void setDir(Boolean dir) {
        isDir = Objects.isNull(isDir) ? Boolean.FALSE : isDir;
    }

}
