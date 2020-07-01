package com.neusoft.bookstore.statisticsinfo.controller;

import com.neusoft.bookstore.statisticsinfo.service.StatisticsInfoService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 煤小二
 * @Date 2020/5/28 18:10
 */
@Api("statisticsinfo")
@RequestMapping("statisticsinfo")
@RestController
public class StatisticsInfoController {

    @Autowired
    private StatisticsInfoService statisticsInfoService;

    /**
     *数据统计
     * @return
     */
    @ApiOperation(value = "数据统计",notes = "数据统计")
    @GetMapping("selctMessage")
    public ResponseVo selctMessage(String loginAccount){
        ResponseVo responseVo=new ResponseVo();
        try {
            //登录
            responseVo=statisticsInfoService.selctMessage(loginAccount);
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
