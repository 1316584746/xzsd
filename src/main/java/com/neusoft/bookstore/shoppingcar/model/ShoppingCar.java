package com.neusoft.bookstore.shoppingcar.model;


import com.neusoft.bookstore.util.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShoppingCar extends BaseModel {

    @ApiModelProperty("商品编码")
    private String skuCode;

    @ApiModelProperty("商品购买数量")
    private Integer shopNum;

    @ApiModelProperty("商家编码")
    private String businessCode;

    @ApiModelProperty("app登录用户id")
    private Integer orderUserId;

    @ApiModelProperty("商品名称")
    private String skuName;

    @ApiModelProperty("售价")
    private BigDecimal salePrice;

    @ApiModelProperty("商品图片")
    private List<String> images;

    @ApiModelProperty("用户id(userId)")
    private Integer userId;
}
