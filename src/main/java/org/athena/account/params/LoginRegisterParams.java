package org.athena.account.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "登录参数")
public class LoginRegisterParams {

    @NotNull(message = "用户名不允许为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "用户名为 4～16 个英文字母和数字组合")
    @ApiModelProperty(name = "userName", value = "用户名称", required = true)
    private String userName;

    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$",
            message = "邮箱格式不符合规则")
    @ApiModelProperty(name = "email", value = "邮箱", notes = "暂未支持")
    private String email;

    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$",
            message = "手机号格式不符合规则")
    @ApiModelProperty(name = "mobile", value = "手机号", notes = "暂未支持")
    private String mobile;

    @NotNull(message = "密码不允许为空")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,30}$",
            message = "密码为8～30位，字母、数字、特称字符组合")
    @ApiModelProperty(name = "passWord", value = "用户密码", required = true)
    private String passWord;

}
