package org.athena.common.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

/**
 * 分页实体
 */
@Getter
@ApiModel(description = "分页实体")
public class PageResp<T> {

    @ApiModelProperty("限制")
    private final long limit;

    @ApiModelProperty("偏移量")
    private final long offset;

    @ApiModelProperty("总记录数")
    private final long totalElements;

    @ApiModelProperty("分页内容")
    private final List<T> content;

    /**
     * 创建分页响应
     */
    private PageResp(List<T> content, long limit, long offset, long total) {
        this.content = content;
        this.limit = limit;
        this.offset = offset;
        this.totalElements = total;
    }

    public static <T> PageResp<T> of(List<T> content, long limit, long offset, long total) {
        return new PageResp<>(content, limit, offset, total);
    }

    public static <T> PageResp<T> of(List<T> content, long limit, long offset) {
        return new PageResp<>(content, limit, offset, 0L);
    }

}
