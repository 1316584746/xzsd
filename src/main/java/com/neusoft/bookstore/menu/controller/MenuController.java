package com.neusoft.bookstore.menu.controller;

import com.neusoft.bookstore.menu.model.Menu;
import com.neusoft.bookstore.menu.service.MenuService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 煤小二
 * @Date 2020/4/27 11:22
 */
@Api("menu")
@RequestMapping("menu")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 新增菜单接口
     * @param menu
     * @return
     */
    @ApiOperation(value = "新增菜单",notes = "新增菜单")
    @PostMapping("addMenu")
    public ResponseVo addMenu(@RequestBody Menu menu){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=menuService.addMenu(menu);
        } catch (Exception e) {
            //处理异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务端后台打印异常
            e.printStackTrace();
        }
        return  responseVo;
    }

    /**
     * 菜单树查询接口
     * @return
     */
    @ApiOperation(value = "菜单树查询",notes = "菜单树查询")
    @GetMapping("listMenuTree")
    public ResponseVo listMenuTree(){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=menuService.listMenuTree();
        } catch (Exception e) {
            //处理异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务端后台打印异常
            e.printStackTrace();
        }
        return  responseVo;
    }

    /**
     * 菜单详情查询接口
     * @return
     */
    @ApiOperation(value = "菜单详情查询",notes = "菜单详情查询")
    @GetMapping("findMenuByCode")
    public ResponseVo findMenuByCode(String menuCode){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=menuService.findMenuByCode(menuCode);
        } catch (Exception e) {
            //处理异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务端后台打印异常
            e.printStackTrace();
        }
        return  responseVo;
    }

    /**
     * 菜单信息修改
     * @param menu
     * @return
     */
    @ApiOperation(value = "菜单信息修改",notes = "菜单信息修改")
    @PostMapping("updateMenuByCode")
    public ResponseVo updateMenuByCode(@RequestBody Menu menu){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=menuService.updateMenuByCode(menu);
        } catch (Exception e) {
            //处理异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务端后台打印异常
            e.printStackTrace();
        }
        return  responseVo;
    }

    /**
     * 删除菜单信息接口
     * @return
     */
    @ApiOperation(value = "删除菜单信息",notes = "删除菜单信息")
    @PostMapping("deleteMenuCode")
    public ResponseVo deleteMenuCode(String menuCode){
        ResponseVo responseVo = new ResponseVo();
        try {
            //新增菜单逻辑的实现
            responseVo=menuService.deleteMenuCode(menuCode);
        } catch (Exception e) {
            //处理异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务端后台打印异常
            e.printStackTrace();
        }
        return  responseVo;
    }
}
