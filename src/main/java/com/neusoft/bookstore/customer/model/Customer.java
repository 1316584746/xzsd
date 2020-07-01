package com.neusoft.bookstore.customer.model;

import com.neusoft.bookstore.util.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author 20141
 * @Date 2020/4/23 11:09
 * @Version 1.0
 */


@Data
public class Customer extends BaseModel {
    @ApiModelProperty("用户登录账号")
    private String userAccount;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户性别 0：女 1：男 2：未知")
    private Integer userSex;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("积分")
    private BigDecimal score;

    @ApiModelProperty("登录源标记（0：表示从app端注册和登录 1：表示从pc端注册和登录）")
    private Integer isAdmin;

    /*
    业务相关的字段
       从前台获取的积分金额，后台需要处理该字段将该值封装到score中去

     */
    @ApiModelProperty("从前台获取的积分金额")
    private String frontScore;

    @ApiModelProperty("获取从前台输入的用户账号或者手机号")
    private String loginAccount;

}
