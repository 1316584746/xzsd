package com.neusoft.bookstore.cate.model;


import com.neusoft.bookstore.util.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Cate extends BaseModel {

    @ApiModelProperty("分类名称")
    private String cateName;

    @ApiModelProperty("分类编码")
    private String cateCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("父辈分类编码")
    private String parentCateCode;

    @ApiModelProperty("创建子集时，前端点击的分类的分类编码")
    private String frontCateCode;

}
