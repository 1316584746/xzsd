package com.neusoft.bookstore.order.service;

import com.neusoft.bookstore.order.model.Order;
import com.neusoft.bookstore.order.model.OrderVo;
import com.neusoft.bookstore.util.ResponseVo;

import java.util.List;

/**
 * @author 煤小二
 * @Date 2020/5/13 10:46
 */
public interface OrderService {
    /**
     * 订单新增
     * @param orderVos
     * @return
     */
    ResponseVo addOrder(List<OrderVo> orderVos);

    /**
     * 列表查询
     * @param order
     * @return
     */
    ResponseVo listOrder(Order order);



    ResponseVo appFindOrderDetails(String pageNum, String pageSize, String loginAccount);

    ResponseVo findOrderDetailByOrderCode(String orderCode);
}
