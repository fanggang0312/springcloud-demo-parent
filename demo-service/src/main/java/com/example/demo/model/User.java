package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: fg
 * @Date: 2020/11/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    //姓名
    private String name;
    //年龄
    private Integer age;
    //性别
    private Integer sex;
    //地址
    private String address;
    //赏金
    private BigDecimal money;
}
