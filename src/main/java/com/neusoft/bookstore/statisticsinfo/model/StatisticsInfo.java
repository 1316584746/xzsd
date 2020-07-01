package com.neusoft.bookstore.statisticsinfo.model;

import com.neusoft.bookstore.util.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 煤小二
 * @Date 2020/5/28 18:15
 */
@Data
public class StatisticsInfo extends BaseModel {

    @ApiModelProperty("用户总数")
    private  Integer customerNum;

    @ApiModelProperty("性别比例")
    private  double sexRatio;

    @ApiModelProperty("分类名称")
    private List<String> cateName;

    @ApiModelProperty("分类下的商品总数")
    private  Integer cateGoodsNum;

    @ApiModelProperty("已下单订单数量")
    private  Integer OrderNum;

    @ApiModelProperty("已完成订单数量")
    private  Integer OrderFinishNum;
}
