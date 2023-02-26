package com.achong.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data   //setter and getter
@ToString   //toString方法
@AllArgsConstructor //全参的构造方法
@NoArgsConstructor  //无参的构造方法
public class Stu {
    private String name;
    private Integer age;

}
