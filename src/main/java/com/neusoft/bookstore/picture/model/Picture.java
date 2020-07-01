package com.neusoft.bookstore.picture.model;



import com.neusoft.bookstore.util.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Picture extends BaseModel {
    @ApiModelProperty("图片路径")
    private String picUrl;

    @ApiModelProperty("图片状态 1：启用 2：禁用")
    private Integer picStatus;

    @ApiModelProperty("从前台获取的用户账号或者是手机号")
    private String loginAccount;

    @ApiModelProperty("图片状态名称")
    private String picStatusName;
}
