package com.neusoft.bookstore.order.mapper;

import com.neusoft.bookstore.order.model.Order;
import com.neusoft.bookstore.order.model.OrderDetails;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 煤小二
 * @Date 2020/5/13 10:45
 */
@Mapper
public interface OrderMapper {

    void addOrderDetail(OrderDetails orderDetails);

    void addOrder(Order order);


    List<Order> listOrder(Order order);


    List<OrderDetails> appFindOrderDetails(String orderCode);

    List<OrderDetails> findOrderDetailByOrderCode(String orderCode);
}
