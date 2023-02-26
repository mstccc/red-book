package com.achong.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data   //setter and getter
@ToString   //toString方法
@AllArgsConstructor //全参的构造方法
@NoArgsConstructor  //无参的构造方法
public class RegisLoginBO {

    @NotBlank(message = "手机号不能为空！")
    @Length(min = 11, max = 11, message = "手机号长度不正确")
    private String mobile;
    @NotBlank(message = "验证码不能为空！")
    private String smsCode;
}
