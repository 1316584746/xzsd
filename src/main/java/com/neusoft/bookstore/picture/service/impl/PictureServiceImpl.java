package com.neusoft.bookstore.picture.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.bookstore.customer.model.Customer;
import com.neusoft.bookstore.picture.mapper.PictureMapper;
import com.neusoft.bookstore.picture.model.Picture;
import com.neusoft.bookstore.picture.service.PictureService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    private PictureMapper pictureMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 轮播图新增
     * @param picture
     * @return
     */
    @Override
    public ResponseVo addPic(Picture picture) {
        /**
         * 图片状态：默认被禁用
         */
        ResponseVo responseVo=new ResponseVo(true, ErrorCode.SUCCESS,"新增成功！");
        //4.获取当前登录人
        Customer customerByRedis=(Customer) redisTemplate.opsForValue().get(picture.getLoginAccount());
        if(customerByRedis!=null){
            //redis 已经保存
            picture.setCreatedBy(customerByRedis.getUserAccount());
        }else {
            //提示需要登录
            responseVo.setMsg("请登录后重试");
            responseVo.setSuccess(false);
            responseVo.setCode(ErrorCode.FAIL);
            return responseVo;
        }

        //新增
        int result=pictureMapper.addPicture(picture);
        if(result!=1){
            responseVo.setMsg("新增失败！");
            responseVo.setSuccess(false);
            responseVo.setCode(ErrorCode.FAIL);
        }
        return responseVo;
    }


    /**
     * 轮播图列表查询（app+pc)
     * @param picture
     * @return
     */
    @Override
    public ResponseVo listPic(Picture picture) {
        /**
         * 1:pc 轮播图列表查询 ：带分页 模糊查询的条件：图片状态
         * 2：app： 轮播图列表查询：不带分页 图片状态：启用
         */
        ResponseVo responseVo=new ResponseVo(true, ErrorCode.SUCCESS,"查询成功！");
//判断分页
        if(picture.getPageNum()!=null&&picture.getPageSize()!=null){
            //pc端列表查询，必须带分页，封装分页信息
            PageHelper.startPage(picture.getPageNum(),picture.getPageSize());
        }
        //直接查询
        List<Picture> pictureList=pictureMapper.listPic(picture);
        //返回
        if(picture.getPageNum()!=null&&picture.getPageSize()!=null){
            //pc端列表查询，返回分页信息
            PageInfo<Picture>  pageInfo=new PageInfo<>(pictureList);
             responseVo.setData(pageInfo);
        }
        else {
            //返回给app 不需要分类
            responseVo.setData(pictureList);
        }
        return responseVo;
    }


    /**
     * 轮播图启用，禁用，删除（app+pc)
     * @param picture
     * @return
     */

    @Override
    public ResponseVo updatePic(Picture picture) {
        /**
         * 1：删除 id
         * 2：启用，禁用 id status（1，或2）
         * 判断依据 status是不是null
         */
        ResponseVo responseVo=new ResponseVo(false, ErrorCode.FAIL,"操作失败！");


        //4.获取当前登录人
        Customer customerByRedis=(Customer) redisTemplate.opsForValue().get(picture.getLoginAccount());
        if(customerByRedis!=null){
            //redis 已经保存
            picture.setUpdatedBy(customerByRedis.getUserAccount());
        }else {
            //提示需要登录
            responseVo.setMsg("请登录后重试");
            return responseVo;
        }
        //校验
        Integer status=picture.getPicStatus();
        if(status!=null&&status!=1&&status!=2){
            responseVo.setMsg("轮播图状态不正确！");
            return responseVo;
        }
int result= pictureMapper.updatePic(picture);
        if(result==1){
            responseVo.setMsg("操作成功");
            responseVo.setSuccess(true);
            responseVo.setCode(ErrorCode.SUCCESS);
        }
        return responseVo;
    }
}
