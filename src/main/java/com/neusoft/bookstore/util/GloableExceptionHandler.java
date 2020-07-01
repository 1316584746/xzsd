package com.neusoft.bookstore.util;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 煤小二
 * @Date 2020/5/15 9:27
 * 全局异常处理,将异常信息返回给前端  (controller层)
 */
@RestControllerAdvice(basePackages = "com.neusoft.bookstore.*.controller")
public class GloableExceptionHandler {

    @ExceptionHandler(Exception.class)//异常处理类型
    public  ResponseVo handlerException(Exception e) {
        ResponseVo<Object> responseVo = new ResponseVo<>(false, ErrorCode.FAIL, "系统异常");
        //自定义异常处理
        if(e instanceof GoodsInfoExcetion){
            responseVo.setMsg(e.getMessage());
        }else {
            responseVo.setMsg("系统异常");
        }

        //服务端后台打印异常
        e.printStackTrace();
        return responseVo;
    }
}
