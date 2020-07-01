package com.neusoft.bookstore.shoppingcar.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.bookstore.customer.model.Customer;
import com.neusoft.bookstore.goods.mapper.GoodsMapper;
import com.neusoft.bookstore.goods.model.Goods;
import com.neusoft.bookstore.shoppingcar.mapper.ShoppingCarMapper;
import com.neusoft.bookstore.shoppingcar.model.ShoppingCar;
import com.neusoft.bookstore.shoppingcar.service.ShoppingCarService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ShoppingCarServiceImpl implements ShoppingCarService {
    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private GoodsMapper goodsMapper;

    /*
     * 添加商品到购物车
     *
     * */
    @Override
    public ResponseVo addShoppingCar(ShoppingCar shoppingCar) {
        /*
        *1.登录
        * 2.商品信息（商品编码）、商家编码、购买人（app登录人），数量默认1 不需要传
        * 3.判断是从详情页加入购物车  还是购物车列表页  加入购物车， 加入时 校验商品库存
        *       从详情页加入购物车： 如果是第一次加入  生成一条新的记录，否则购物车数据量加 1
        *       购物车到列表页 加入购物车： 更新数据量加1
        * */

        ResponseVo responseVo=new ResponseVo(false, ErrorCode.FAIL,"加入购物车失败");
        //1.获取登录人
        //从redis中获取当前登录人的用户信息
        Customer customerByRedis=(Customer) redisTemplate.opsForValue().get(shoppingCar.getLoginAccount());
        String name= shoppingCar.getLoginAccount();
        if (customerByRedis!=null){
            //redis已经保存
            shoppingCar.setOrderUserId(customerByRedis.getId());
            shoppingCar.setCreatedBy(customerByRedis.getUserAccount());
            shoppingCar.setUpdatedBy(customerByRedis.getUserAccount());

        }else {
            //提示需要登录
            responseVo.setMsg("请登陆后重试");
            return responseVo;
        }

        //2.商品信息（商品编码）、商家编码、购买人（app登录人），数量默认1 不需要传
        //校验商品信息 和商家信息
        if (StringUtils.isEmpty(shoppingCar.getSkuCode()) || StringUtils.isEmpty(shoppingCar.getBusinessCode())){
            responseVo.setMsg("商品信息不完整！");
            return responseVo;
        }
        //3.判断是从详情页加入购物车  还是购物车列表页  加入购物车， 加入时 校验商品库存
        ShoppingCar shoppingCarByDb=shoppingCarMapper.findGoodsFromCar(shoppingCar);
        if (shoppingCarByDb==null){
            //购物车没有记录，新增一条
            //校验商品库存
            Goods goods=goodsMapper.findGoodsBySkuCode(shoppingCar.getSkuCode());
            if (goods.getStoreNum()<=0){
                responseVo.setMsg("商品库存不足，无法添加！");
                return responseVo;
            }
            int result=shoppingCarMapper.addShoppingCar(shoppingCar);
            if (result==1){
                responseVo.setSuccess(true);
                responseVo.setCode(ErrorCode.SUCCESS);
                responseVo.setMsg("加入购物车成功！");
                return responseVo;
            }
        }else {
            //购物车有记录，更新一条
            //校验商品库存
            Goods goods=goodsMapper.findGoodsBySkuCode(shoppingCar.getSkuCode());
            //依然需要判断是从商品详情中  添加还是 到列表的添加和减少
            //依据就是 商品详情： shopNum 传null  列表的添加和减少 shopNum就是原本购物车中商品数量加1或者减1后的值
            if (shoppingCar.getShopNum() == null){
                //从详情页  默认加1
                //当前购物车中已有的商品数量
                Integer shopNum=shoppingCarByDb.getShopNum();
                //比较库存
                if ((shopNum+1) > goods.getStoreNum()){
                    responseVo.setMsg("商品库存不足，无法添加！");
                    return responseVo;
                }else {
                    //可以添加
                    shoppingCar.setShopNum(shopNum+1);
                }
            }else {
                //购物车列表
                if (shoppingCar.getShopNum()>goods.getStoreNum()){
                    responseVo.setMsg("商品库存不足，无法添加！");
                    return responseVo;
                }
            }

            //修改商品库存
            int result=shoppingCarMapper.updateShoppingCar(shoppingCar);
            if (result==1){
                responseVo.setSuccess(true);
                responseVo.setCode(ErrorCode.SUCCESS);
                responseVo.setMsg("加入购物车成功！");
                return responseVo;
            }


        }
        return responseVo;
    }


    /*
     * 购物车列表
     *
     * */
    @Override
    public ResponseVo findGoodsFromCar(Integer userId, Integer pageSize, Integer pageNum) {
        /*
        * 分页
        * */
        ResponseVo responseVo=new ResponseVo(true,ErrorCode.SUCCESS,"查询成功!");
        if (pageSize!=null && pageNum!=null){
            PageHelper.startPage(pageNum,pageSize);
        }
        List<ShoppingCar> shoppingCarList=shoppingCarMapper.listGoodsFromCar(userId);
        //加载对应的商品图片  遍历查找
        if (shoppingCarList!=null && shoppingCarList.size()>0){
            //查询图片
            for (int i=0;i<shoppingCarList.size();i++){
                //根据skuCode查询图片
                ShoppingCar car=shoppingCarList.get(i);
                List<String> goodImages=goodsMapper.findImagesBySkuCode(car.getSkuCode());
                car.setImages(goodImages);
            }
        }
        //返回
        if (pageSize!=null&&pageNum!=null){
            PageInfo<ShoppingCar> pageInfo=new PageInfo<>(shoppingCarList);
            responseVo.setData(pageInfo);
        }else {
            responseVo.setData(shoppingCarList);
        }
        return responseVo;
    }


    /*
     * 删除购物车商品
     *
     * */
    @Override
    public ResponseVo deleteGoodsFromCar(ShoppingCar shoppingCar) {
        /*
        * 1.登录
        * 2.校验必传数据
        * 3.删除 根据 商品编码，商家编码，购买人id（物理删除）
        * */
        ResponseVo responseVo=new ResponseVo(false,ErrorCode.FAIL,"删除失败!");
        //1.获取登录
        Customer customerByRedis=(Customer) redisTemplate.opsForValue().get(shoppingCar.getLoginAccount());
        if (customerByRedis!=null){
            //redis已经保存
            shoppingCar.setOrderUserId(customerByRedis.getId());

        }else {
            //提示需要登录
            responseVo.setMsg("请登陆后重试！");
            return responseVo;
        }
        //2.校验必传数据
        //校验商品信息  和商家信息
        if (StringUtils.isEmpty(shoppingCar.getSkuCode()) || StringUtils.isEmpty(shoppingCar.getBusinessCode())){
            responseVo.setMsg("商品信息不完整！");
            return responseVo;
        }
        //3.删除，根据商品编码，商家编码，购买人id，物理删除
        int result=shoppingCarMapper.deleteGoodsFromCar(shoppingCar);
        if (result==1){
            responseVo.setSuccess(true);
            responseVo.setCode(ErrorCode.SUCCESS);
            responseVo.setMsg("删除成功！");
            return responseVo;
        }

        return responseVo;
    }
}
