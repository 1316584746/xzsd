package com.neusoft.bookstore.goods.controller;


import com.neusoft.bookstore.goods.model.Goods;

import com.neusoft.bookstore.goods.service.GoodsService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api("goods")
@RestController
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    /**
     * 查询全部商家
     * @param
     * @return
     */

    @ApiOperation(value = "查询全部商家",notes = "查询全部商家")
    @GetMapping("listBusiness")
    public ResponseVo listBusinss(){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.listBusinss();
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }



    /**
     * 图片上传
     * @param
     * @return
     */

    @ApiOperation(value = "图片上传",notes = "图片上传")
    @PostMapping("uploadImage")
    public ResponseVo uploadImage(MultipartFile file){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.uploadImage(file);
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }



    /**
     * 商品新增
     *问题 : 第二次添加失败!!! 父级编码是空值
     * @param
     * @return
     */

    @ApiOperation(value = "商品新增",notes = "商品新增")
    @PostMapping("addGoods")
    public ResponseVo addGoods(@RequestBody Goods goods){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.addGoods(goods);
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }



    /**
     * 商品列表查询
     * @param
     * @return
     */

    @ApiOperation(value = "商品列表查询",notes = "商品列表查询")
    @PostMapping("listGoods")
    public ResponseVo listGoods(@RequestBody Goods goods){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.listGoods(goods);
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }


    /**
     * 商品详情查询
     * @param
     * @return
     */

    @ApiOperation(value = "商品详情查询",notes = "商品详情查询")
    @GetMapping("findGoodsBySkuCode")
    public ResponseVo findGoodsBySkuCode(String skuCode){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.findGoodsBySkuCode(skuCode);
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }



    /**
     * 根据商家编码商家详情查询
     * @param
     * @return
     */

    @ApiOperation(value = "根据商家编码商家详情查询",notes = "根据商家编码商家详情查询")
    @GetMapping("findBusinessByCode")
    public ResponseVo findBusinessByCode(String businessCode){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.findBusinessByCode(businessCode);
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }


    /**
     * 根据商品编码修改商品信息
     * @param
     * @return
     */

    @ApiOperation(value = "根据商品编码修改商品信息",notes = "根据商品编码修改商品信息")
    @PostMapping("updateGoodsInfo")
    public ResponseVo updateGoodsInfo(@RequestBody Goods goods){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.updateGoodsInfo(goods);
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }


    /**
     * 根据商品编码删除商品信息
     * @param
     * @return
     */

    @ApiOperation(value = "根据商品编码删除商品信息",notes = "根据商品编码删除商品信息")
    @GetMapping("deleteGoods")
    public ResponseVo deleteGoods(String skuCode,String loginAccount){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.deleteGoods(skuCode,loginAccount);
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }

    /**
     * 商品的上架和下架
     * @param
     * @return
     */

    @ApiOperation(value = "商品的上架和下架",notes = "商品的上架和下架")
    @GetMapping("updateGoodStatus")
    public ResponseVo updateGoodStatus(String skuCode,String loginAccount,String status){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=goodsService.updateGoodStatus(skuCode,loginAccount,status);
        }catch (Exception e){
            //处理异常 提示前台 服务器有异常
            responseVo.setCode(ErrorCode.SERVER_EXCEPTION_CODE);
            responseVo.setSuccess(false);
            responseVo.setMsg("系统异常");
            //服务器端后台 打印异常
            e.printStackTrace();
        }
        return responseVo;
    }

}
