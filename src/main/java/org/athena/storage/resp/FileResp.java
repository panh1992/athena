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
@ApiModel(description = "文件列表响应实体")
public class FileResp {

    @ApiModelProperty("文件主键")
    private Long fileId;

    @ApiModelProperty("文件所属存储空间名称")
    private String storeSpace;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("用户角度文件大小 单位 byte")
    private Long fileSize;

    @ApiModelProperty("是否为目录")
    private Boolean isDir;

    @ApiModelProperty("文件的校验码")
    private String checkSum;

    @ApiModelProperty("文件格式 以文件扩展名为依据")
    private String format;

    @ApiModelProperty("文件状态  new uploading available failed deleted")
    private String status;

    @ApiModelProperty("创建时间")
    private Instant createTime;

    @ApiModelProperty("描述信息")
    private String description;

}
