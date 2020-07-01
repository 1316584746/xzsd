package com.neusoft.bookstore.order.model;

import com.neusoft.bookstore.goods.model.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetails extends Goods {


  @ApiModelProperty("订单编码")
  private String orderCode;


  @ApiModelProperty("商品编码")
  private String skuCode;


  @ApiModelProperty("购买数量")
  private Integer shopNum;


  @ApiModelProperty("该商品总金额")
  private BigDecimal skuAmount;

  private String imagesPath;

  private List<String>  images;
}
