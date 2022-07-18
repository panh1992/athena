package org.athena.account.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息参数")
public class UserInfoParams {

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$",
            message = "邮箱格式不符合规则")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$",
            message = "手机号格式不符合规则")
    @ApiModelProperty(value = "手机号", notes = "暂未支持")
    private String mobile;

    @ApiModelProperty(value = "个人简介")
    private String profile;

}
