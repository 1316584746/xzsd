package com.neusoft.bookstore.goods.model;

import com.neusoft.bookstore.util.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Goods extends BaseModel {

    @ApiModelProperty("商品名称")
    private String skuName;

    @ApiModelProperty("标准书号")
    private String isbn;

    @ApiModelProperty("一级商品分类")
    private String firstCateCode;

    @ApiModelProperty("二级商品分类编码")
    private String secondCateCode;

    @ApiModelProperty("广告词")
    private String skuAd;

    @ApiModelProperty("商品介绍")
    private String skuIntroduction;

    @ApiModelProperty("商家编码")
    private String businessCode;

    @ApiModelProperty("商品库存")
    private int storeNum;

    @ApiModelProperty("定价")
    private BigDecimal costPrice;

    @ApiModelProperty("售价")
    private BigDecimal salePrice;

    @ApiModelProperty("0:在售 1：已下架  2：未发布")
    private Integer skuStatus;

    @ApiModelProperty("商品编码")
    private String skuCode;

    @ApiModelProperty("商品的销售数量")
    private int saleNum;

    @ApiModelProperty("商品的上架时间")
    private Date saleTime;


    @ApiModelProperty("从前端获取定价")
    private String frontCostPrice;

    @ApiModelProperty("从前端获取销售价")
    private String frontSalePrice;

    @ApiModelProperty("从前台获取的用户账号或者是手机号")
    private String loginAccount;

    @ApiModelProperty("商品图片")
    private List<String> images;

    @ApiModelProperty("一级分类名称")
    private String firstCateName;

    @ApiModelProperty("二级分类名称")
    private String secondCateName;

    @ApiModelProperty("商家名称")
    private String businessName;

    @ApiModelProperty("状态名称")
    private String skuStatusName;
}
