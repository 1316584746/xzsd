package com.neusoft.bookstore.order.model;

import lombok.Data;

/**
 * @author 煤小二
 * @Date 2020/5/13 11:31
 * 接收订单时,接收前端传值的结果
 *
 * 前端---xxxxVO-->controller-----xxxxDTO--->  service ---xxxxDO--> dao
 * xxxxVo  :前端传输层
 * xxxxDTO : service传输层
 * xxxxDO  : dao传输层
 */
@Data
public class OrderVo {
    /**
     * 接收前端的参数
     */
    private String skuCode;


    private String businessCode;


    private String loginAccount;

    private  Integer shopNum;

    public OrderVo() {
    }

    public OrderVo(String skuCode, String businessCode, Integer shopNum, String loginAccount) {
        this.skuCode = skuCode;
        this.businessCode = businessCode;
        this.loginAccount = loginAccount;
        this.shopNum = shopNum;
    }
}
