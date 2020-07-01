package com.neusoft.bookstore.customer.controller;

import com.neusoft.bookstore.customer.model.Customer;
import com.neusoft.bookstore.customer.service.CustomerService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 20141
 * @Date 2020/4/23 10:54
 * @Version 1.0
 */


@Api("customer")
@RequestMapping("customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*用户新增注册
     customer：封装了所有前端页面的用户信息
     */

    @ApiOperation(value = "注册新增用户", notes = "app端和pc端新用户注册")
    @PostMapping("addCustomer")
    public ResponseVo addCustomer(@RequestBody Customer customer) {
        ResponseVo responseVo = new ResponseVo();

        try {
            responseVo = customerService.addCustomer(customer);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }

    /*
    用户登录
     */
    @ApiOperation(value = "app和pc端用户登录",notes = "app和pc端用户登录")
    @PostMapping("login")
    public ResponseVo login(@RequestBody Customer customer){
        ResponseVo responseVo = new ResponseVo();
        try {
           //登录
            responseVo=customerService.login(customer);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }
/*
   用户退出
 */
    @ApiOperation(value = "app和pc端用户退出",notes = "app和pc端用户退出")
    @GetMapping ("loginOut")
    public ResponseVo loginOut(String userAccount){
        ResponseVo responseVo = new ResponseVo();
        try {
            //登录
            responseVo=customerService.loginOut(userAccount);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }
/*
    用户列表查询
    有模糊查询的情况下一般使用post
 */
    @ApiOperation(value = "用户列表查询",notes = "用户列表查询")
    @PostMapping ("listCustomers")
    public ResponseVo listCustomers(@RequestBody Customer customer){
        ResponseVo responseVo = new ResponseVo();
        try {
            //登录
            responseVo=customerService.listCustomers(customer);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }

    /*
    用户详情查询
 */
    @ApiOperation(value = "用户详情查询",notes = "用户详情查询")
    @GetMapping ("findCustomerById")
    public ResponseVo findCustomerById(Integer id){
        ResponseVo responseVo = new ResponseVo();
        try {
            //登录
            responseVo=customerService.findCustomerById(id);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }

    /*
    用户修改
    根据用户id去修改
 */
    @ApiOperation(value = "用户修改",notes = "用户修改")
    @PostMapping ("findCustomerById")
    public ResponseVo updateCustomerById(@RequestBody Customer customer){
        ResponseVo responseVo = new ResponseVo();
        try {
            //登录
            responseVo=customerService.updateCustomerById(customer);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }

    /*
      根据用户id去删除(逻辑删除)
      用户删除
     */
    @ApiOperation(value = "用户删除",notes = "用户删除")
    @GetMapping ("deleteCustomerById")
    public ResponseVo deleteCustomerById(Integer id){
        ResponseVo responseVo = new ResponseVo();
        try {
            //登录
            responseVo=customerService.deleteCustomerById(id);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }

    /*
      根据用户id去更新密码（app+pc）
      用户修改密码
      originPwd:原始密码
      newPwd：新密码
      userId:修改密码用户的id
      userAccount：登录人的账号
     */
    @ApiOperation(value = "根据用户id，更新密码",notes = "根据用户id，更新密码")
    @GetMapping ("updatePwdById")
    public ResponseVo updatePwd(String originPwd, String newPwd, Integer userId, String userAccount){
        ResponseVo responseVo = new ResponseVo();
        try {
            //登录
            responseVo=customerService.updatePwd(originPwd,newPwd,userId,userAccount);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }

    /*
      用户积分
      newScore:修改后的积分
      userId：修改用户的id
      userAccount：登录人的账号
     */
    @ApiOperation(value = "修改用户积分",notes = "修改用户积分")
    @GetMapping ("updateScore")
    public ResponseVo updateScore(String newScore,Integer userId,String userAccount){
        ResponseVo responseVo = new ResponseVo();
        try {
            //登录
            responseVo=customerService.updateScore(newScore,userId,userAccount);
        } catch (Exception e) {
            //处理异常，提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //在服务端后台打印异常
            e.printStackTrace();
        }
        return responseVo;
    }


}
