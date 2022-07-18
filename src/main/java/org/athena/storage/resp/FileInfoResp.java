package org.athena.storage.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "文件详细信息响应实体")
public class FileInfoResp extends FileResp {

    @ApiModelProperty("用户角度文件存储路径")
    private String filePath;

    @Builder(builderMethodName = "infoBuilder")
    public FileInfoResp(Long fileId, String storeSpace, String fileName, Long fileSize, Boolean isDir, String checkSum,
                        String format, String status, Instant createTime, String description, String filePath) {
        super(fileId, storeSpace, fileName, fileSize, isDir, checkSum, format, status, createTime, description);
        this.filePath = filePath;
    }

}
