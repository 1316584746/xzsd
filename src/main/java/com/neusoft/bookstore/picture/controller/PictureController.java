package com.neusoft.bookstore.picture.controller;


import com.neusoft.bookstore.picture.model.Picture;
import com.neusoft.bookstore.picture.service.PictureService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("picture")
@RestController
@RequestMapping("picture")
public class PictureController {
    @Autowired
    private PictureService pictureService;


    /**
     * 轮播图新增
     * @param picture
     * @return
     */
    @ApiOperation(value = "轮播图新增",notes = "轮播图新增")
    @PostMapping("addPic")
    public ResponseVo addPic(@RequestBody Picture picture){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=pictureService.addPic(picture);
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
     * 轮播图列表查询（app+pc)
     * @param picture
     * @return
     */
    @ApiOperation(value = "轮播图列表查询",notes = "轮播图列表查询")
    @PostMapping("listPic")
    public ResponseVo listPic(@RequestBody Picture picture){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=pictureService.listPic(picture);
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
     * 轮播图启用，禁用，删除（app+pc)
     * @param picture
     * @return
     */
    @ApiOperation(value = "轮播图启用，禁用，删除",notes = "轮播图启用，禁用，删除")
    @PostMapping("updatePic")
    public ResponseVo updatePic(@RequestBody Picture picture){
        ResponseVo responseVo=new ResponseVo();
        try{
            //登录
            responseVo=pictureService.updatePic(picture);
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
