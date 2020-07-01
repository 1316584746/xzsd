package com.neusoft.bookstore.statisticsinfo.service.impl;

import com.neusoft.bookstore.customer.model.Customer;
import com.neusoft.bookstore.statisticsinfo.mapper.StatisticsInfoMapper;
import com.neusoft.bookstore.statisticsinfo.service.StatisticsInfoService;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import okhttp3.internal.Internal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.DecimalFormat;
import java.util.*;

/**
 * @author 煤小二
 * @Date 2020/5/28 18:13
 */
@Service
public class StatisticsInfoServiceImpl  implements StatisticsInfoService {

    @Autowired
    private StatisticsInfoMapper statisticsInfoMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    /**
     * 统计信息查询
     * @return
     */
    @Transactional
    @Override
    public ResponseVo selctMessage(String loginAccount) {
        /**
         * 1.检测是否用户登录
         * 2.查询所需要的信息的数量统计
         */
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "查询失败!");
        //4.获取当前登录人
        Customer customerByRedis=(Customer) redisTemplate.opsForValue().get(loginAccount);
        if(customerByRedis!=null){
            //redis 已经保存
           //todo 后期
        }else {
            //提示需要登录
            responseVo.setMsg("请登录后重试");
            return responseVo;
        }
        //定义一个集合装数据
        Map <String,Object> Message=new HashMap<>();


        //1.用户总数的查询
       int customerNum=statisticsInfoMapper.selectCustomer();
       Message.put("customerNum",customerNum);
       //2.性别比例
        DecimalFormat df   = new DecimalFormat("######0.00%");
        int man=statisticsInfoMapper.findSexMan();
       int women=statisticsInfoMapper.findSexWomen();
       double sexManRatio=(double) man/customerNum;
       double sexWomenRatio=(double)women/customerNum;

        Message.put("sexManRatio",df.format(sexManRatio));
        Message.put("sexWomenRatio",df.format(sexWomenRatio));

        //3.分类名称
        List<String> cateName=statisticsInfoMapper.cateName();
        Message.put("cateName",cateName);
        Message.put("cateGoodsNum",new ArrayList<>());
       for (int i = 0; i <cateName.size() ; i++) {
            //4.分类名称下的商品数量
           List<Integer> cateGoodsNum =statisticsInfoMapper.findCateGoodsNum(cateName.get(i));
           List<Integer> list=new ArrayList();
           for (int j = 0; j <cateGoodsNum.size() ; j++) {
               list.add(j,cateGoodsNum.get(j));
           }

              List<Integer> list2 =  (List<Integer>)Message.get("cateGoodsNum");
              list2.addAll(list);
           //  Message.put("cateGoodsNum",list2);



       }
       //5.订单数总量 已下单  已完成
      int orderNum=statisticsInfoMapper.orderNum();
        System.out.println(orderNum);
        Message.put("orderNum",orderNum);
        int orderFinishNum=statisticsInfoMapper.orderFinishNum();
        System.out.println(orderFinishNum);
        Message.put("orderFinishNum",orderFinishNum);

        responseVo.setSuccess(true);
        responseVo.setCode(ErrorCode.SUCCESS);
        responseVo.setData(Message);
        responseVo.setMsg("查询成功!");
        return responseVo;
    }
}
