package com.neusoft.bookstore.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 煤小二
 * @Date 2020/4/23 11:23
 * implements Serializable  序列化实体类
 */
@Data
public class BaseModel  implements Serializable {

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("作废标记: 0:否(未删除)  1:是 (已删除)")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("更新时间")
    private Date updatedTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("当前页")
    private  Integer  pageNum;

    @ApiModelProperty("每页显示条数")
    private  Integer pageSize;

    @ApiModelProperty("获取前台输入的用户账号或者手机号")
    private  String loginAccount;
}
