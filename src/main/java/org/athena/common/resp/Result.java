package org.athena.common.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "统一响应数据实体")
public class Result<T> {

    @ApiModelProperty("编码")
    private String code = "SUCCESS";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty("响应数据")
    private T data;

    @ApiModelProperty("描述信息")
    private String message;

    /**
     * 创建成功响应结果
     */
    public static <T> Result<T> build() {
        Result<T> result = new Result<>();
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 根据返回数据创建响应结果
     *
     * @param data 返回数据
     */
    public static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 根据返回结果及返回描述创建响应结果
     *
     * @param data    返回数据
     * @param message 描述信息
     */
    public static <T> Result<T> build(T data, String message) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    /**
     * 根据返回 错误代码、结果、描述 创建响应结果
     *
     * @param code    错误代码
     * @param data    返回数据
     * @param message 描述信息
     */
    public static <T> Result<T> build(String code, T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

}
