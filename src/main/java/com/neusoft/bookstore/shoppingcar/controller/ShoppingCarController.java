package com.neusoft.bookstore.shoppingcar.controller;


import com.neusoft.bookstore.shoppingcar.model.ShoppingCar;
import com.neusoft.bookstore.shoppingcar.service.ShoppingCarService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("shoppingcar")
@RequestMapping("shoppingcar")
@RestController
public class ShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;


    /*
     * 添加商品到购物车
     *
     * */

    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车")
    @PostMapping("addShoppingCar")
    public ResponseVo addShoppingCar(@RequestBody ShoppingCar shoppingCar){
        ResponseVo responseVo=new ResponseVo();
        try {
            //登录
            responseVo=shoppingCarService.addShoppingCar(shoppingCar);
        }catch (Exception e){
            //处理异常 提示前台 服务端 有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务端后台  打印异常
            e.printStackTrace();
        }
        return responseVo;
    }


    /*
     * 购物车列表
     *
     * */

    @ApiOperation(value = "购物车列表",notes = "购物车列表")
    @GetMapping("findGoodsFromCar")
    public ResponseVo findGoodsFromCar(Integer userId, Integer pageSize, Integer pageNum){
        ResponseVo responseVo=new ResponseVo();
        try {
            //登录
            responseVo=shoppingCarService.findGoodsFromCar(userId,pageSize,pageNum);
        }catch (Exception e){
            //处理异常 提示前台 服务端 有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务端后台  打印异常
            e.printStackTrace();
        }
        return responseVo;
    }



    /*
     * 删除购物车商品
     *
     * */

    @ApiOperation(value = "删除购物车商品",notes = "删除购物车商品")
    @PostMapping("deleteGoodsFromCar")
    public ResponseVo deleteGoodsFromCar(@RequestBody ShoppingCar shoppingCar){
        ResponseVo responseVo=new ResponseVo();
        try {
            //登录
            responseVo=shoppingCarService.deleteGoodsFromCar(shoppingCar);
        }catch (Exception e){
            //处理异常 提示前台 服务端 有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务端后台  打印异常
            e.printStackTrace();
        }
        return responseVo;
    }
}
