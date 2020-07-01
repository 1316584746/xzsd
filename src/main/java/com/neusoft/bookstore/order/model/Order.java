package com.neusoft.bookstore.order.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.neusoft.bookstore.util.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class Order  extends BaseModel {

  @ApiModelProperty("订单编码")
  private String orderCode;

  @ApiModelProperty("商品编码")
  private String businessCode;

  @ApiModelProperty("下单人id")
  private Integer orderUserId;

  @ApiModelProperty("订单状态 0:已下单 1:已发货 2: 已完成未评价 4:已评价 5:已取消")
  private Integer orderStatus;

  @ApiModelProperty("支付状态 0:未支付 1:已支付 2:退款中 3:已退款")
  private Integer payStatus;

  @ApiModelProperty("订单总金额")
  private BigDecimal orderAmount;

  @ApiModelProperty("订单状态名称")
  private String orderStatusName;

  @ApiModelProperty("支付状态名称")
  private String payStatusName;

  @ApiModelProperty("用户姓名")
  private  String userName;

  @ApiModelProperty("用户手机")
  private  String phone;

  @ApiModelProperty("下单开始时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date orderStartTime;


  @ApiModelProperty("下单完成时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private  Date  orderEndTime;

  private List<OrderDetails>  orderDetails;

}
