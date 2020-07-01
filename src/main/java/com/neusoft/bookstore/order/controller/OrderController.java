package com.neusoft.bookstore.order.controller;

import com.neusoft.bookstore.order.model.Order;
import com.neusoft.bookstore.order.model.OrderVo;
import com.neusoft.bookstore.order.service.OrderService;
import com.neusoft.bookstore.util.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 煤小二
 * @Date 2020/5/13 10:44
 */
@Api("order")
@RequestMapping("order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单创建接口
     * @param
     * @return
     */
    @ApiOperation(value = "订单创建",notes = "订单创建")
    @PostMapping("addOrder")
    public ResponseVo addOrder(@RequestBody List<OrderVo> orderVos){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=orderService.addOrder(orderVos);
        } catch (Exception e) {
            //处理异常
            throw  e;
        }
        return  responseVo;
    }

    /**
     * 订单列表查询
     * @return
     */
    @ApiOperation(value = "订单列表查询",notes = "订单列表查询")
    @PostMapping("listOrder")
    public ResponseVo listOrder(@RequestBody Order order){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=orderService.listOrder(order);
        } catch (Exception e) {
            //处理异常
            throw  e;
        }
        return  responseVo;
    }

    /**
     * 订单详情查询
     * @param orderCode
     * @return
     */
    @ApiOperation(value = "订单详情查询",notes = "订单详情查询")
    @GetMapping("findOrderDetailByOrderCode")
    public ResponseVo findOrderDetailByOrderCode(String orderCode){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=orderService.findOrderDetailByOrderCode(orderCode);
        } catch (Exception e) {
            //处理异常
            throw  e;
        }
        return  responseVo;
    }

    /**
     * App查询订单接口 需要修改  图片返回应该为list集合
     * @param
     * @return
     */
    @ApiOperation(value = "App查询订单",notes = "App查询订单")
    @GetMapping("appFindOrderDetails")
    public ResponseVo appFindOrderDetails(String pageNum, String pageSize, String loginAccount){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=orderService.appFindOrderDetails(pageNum,pageSize,loginAccount);
        } catch (Exception e) {
            //处理异常
            throw  e;
        }
        return  responseVo;
    }


}
