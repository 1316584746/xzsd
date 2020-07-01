package com.neusoft.bookstore.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.bookstore.customer.mapper.CustomerMapper;
import com.neusoft.bookstore.customer.model.Customer;
import com.neusoft.bookstore.goods.mapper.GoodsMapper;
import com.neusoft.bookstore.goods.model.Goods;
import com.neusoft.bookstore.order.mapper.OrderMapper;
import com.neusoft.bookstore.order.model.Order;
import com.neusoft.bookstore.order.model.OrderDetails;
import com.neusoft.bookstore.order.model.OrderVo;
import com.neusoft.bookstore.order.service.OrderService;
import com.neusoft.bookstore.shoppingcar.mapper.ShoppingCarMapper;
import com.neusoft.bookstore.shoppingcar.model.ShoppingCar;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.GoodsInfoExcetion;
import com.neusoft.bookstore.util.ResponseVo;
import com.neusoft.bookstore.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 煤小二
 * @Date 2020/5/13 10:46
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Autowired
    private CustomerMapper customerMapper;
    /**
     * 订单新增
     *
     * @param orderVos
     * @return
     */
    @Override
    @Transactional(rollbackFor =Exception.class)
    public ResponseVo addOrder(List<OrderVo> orderVos) {
        /**
         * 1.登录
         * 2.数据校验
         * 3.业务逻辑: 商品状态 库存 金额
         * 4.订单创建
         *   0:商家拆分订单
         *   1:订单总金额 每个商品的总金额
         * 5.订单完成后,修改库存 用户余额  增加销售数量
         */
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "订单创建失败!");
        if (orderVos == null || orderVos.size() <= 0) {
            responseVo.setMsg("未购买任何商品!");
            return responseVo;
        }

        //1.获取登录人信息
        //取出登录账号
        String loginAccount = orderVos.get(0).getLoginAccount();
        Integer userId = null;
        Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(loginAccount);
        if (customerByRedis != null) {
            //Redis里有数据的情况
            userId = customerByRedis.getId();
        } else {
            //没有数据的情况，提示登陸
            responseVo.setMsg("请先登录后!");
            return responseVo;
        }

        //取出用户余额
        BigDecimal score = customerByRedis.getScore();
       /* Customer customer = customerMapper.findCustomerById(userId);
        BigDecimal score = customer.getScore();*/
        //定义总价格  所有订单的价格
        BigDecimal orderAmountAll = new BigDecimal(0.0);
        //找出有多少个商家 再找出商家下有多少商品
        //存放 商家和对应的商品集合 key:商家code, value:商家对应的商品集合
        Map<String, List<OrderVo>> hashMap = orderVos.stream().collect(Collectors.groupingBy(OrderVo::getBusinessCode));
       //遍历
        for(Map.Entry<String,List<OrderVo>> entry:hashMap.entrySet()){
             //取出商家编码
            String bussinessCode = entry.getKey();
            //购买商品集合
            List<OrderVo> voList = entry.getValue();
            //计算订单总金额
            BigDecimal orderAmount = new BigDecimal(0.0);
            //生成订单编码
            String orderCode = StringUtil.getCommonCode(2);

            //处理订单详情
            for (int i = 0; i <voList.size() ; i++) {
              //商品状态,商品库存 用户余额
                OrderVo orderVo = voList.get(i);
                //校验商品是否已经下架(商品详情)
                Goods goodsBySkuCode = goodsMapper.findGoodsBySkuCode(orderVo.getSkuCode());
               //商品状态校验
                if(goodsBySkuCode==null || goodsBySkuCode.getSkuStatus()!=0 ){

                 throw new GoodsInfoExcetion("商品已经下架,无法购买!");
                }
                //商品库存校验
                if(orderVo.getShopNum()> goodsBySkuCode.getStoreNum() ){

                    throw new GoodsInfoExcetion("商品库存不足,无法购买!");
                }
                //用户余额校验
                //计算每种商品的总价格
                //System.out.println(orderVo.getShopNum());
               BigDecimal  skuAmount=goodsBySkuCode.getSalePrice().multiply(new BigDecimal(orderVo.getShopNum()));
                //计算订单价格
                orderAmount=orderAmount.add(skuAmount);
                //计算总价
                orderAmountAll=orderAmountAll.add(skuAmount);

                //比较用户余额和总价
                /**
                 * a compareTo(b)  : 0, 1, -1
                 * a>b  1
                 * a<b  -1
                 * a=b  0
                 */
                if(orderAmountAll.compareTo(score)==1){
                    responseVo.setMsg("余额不足,无法购买!");
                    return  responseVo;
                }

                //创建订单详情
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderCode(orderCode);
                orderDetails.setShopNum(orderVo.getShopNum());
                orderDetails.setSkuCode(orderVo.getSkuCode());
                orderDetails.setSkuAmount(skuAmount);
                orderDetails.setCreatedBy(loginAccount);
                orderMapper.addOrderDetail(orderDetails);

              //减少商品库存,增加商品的销售数量
              Map<String,Object> map=new HashMap<>();
              map.put("skuCode",orderVo.getSkuCode());
              map.put("shopNum",orderVo.getShopNum());
              goodsMapper.updateGoodsStoreAndSaNum(map);

              //删除购物车
                ShoppingCar shoppingCar = new ShoppingCar();
                shoppingCar.setBusinessCode(bussinessCode);
                shoppingCar.setSkuCode(orderVo.getSkuCode());
                shoppingCar.setOrderUserId(userId);
                shoppingCarMapper.deleteGoodsFromCar(shoppingCar);
            }
            //生成订单表
            Order order = new Order();
            order.setOrderUserId(userId);
            order.setBusinessCode(bussinessCode);
            order.setOrderAmount(orderAmount);
            order.setOrderStatus(0);
            order.setPayStatus(1);
            order.setOrderCode(orderCode);
            orderMapper.addOrder(order);
           /*
            选中多行代码，按住tab就会同时向右移动
            选中多行代码，按住shift+tab就会同时向左移动
            选中多行代码，按住Ctrl+Alt+L就会格式化代码*/

        }
        //用户余额减少
         score = score.subtract(orderAmountAll);
        Map<Object, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("score",score);
        map.put("userAccount",loginAccount);
        customerMapper.updateScoreById(map);
         responseVo.setMsg("创建成功!");
         responseVo.setCode(ErrorCode.SUCCESS);
         responseVo.setSuccess(true);

        return responseVo;
    }

    /**
     * 订单列表查询
     * @param order
     * @return
     */
    @Override
    public ResponseVo listOrder(Order order) {
        /**
         * 1.获得登录人信息
         * 2.查询所有订单列表  模糊查询 分页
         * 3.app: 查询该用户所有的订单
         *
         */
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "查询成功!");
        //       1.获得登录人信息
        String userName="";
        String phone="";
        Integer userId=null;
        Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(order.getLoginAccount());
        if (customerByRedis != null) {
            //Redis里有数据的情况
             userName = customerByRedis.getUserName();
             phone=customerByRedis.getPhone();
             userId= customerByRedis.getId();
        } else {
            //没有数据的情况，提示登陸
            responseVo.setMsg("请先登录后!");
            return responseVo;
        }
//        分页操作
        PageHelper.startPage(order.getPageNum(),order.getPageSize());
        List<Order> orderList = orderMapper.listOrder(order);
        PageInfo<Order> orderPageInfo = new PageInfo<>(orderList);
         responseVo.setData(orderPageInfo);
        return responseVo;
    }

    /**
     * 订单详情查询
     * @param orderCode
     * @return
     */
    @Override
    public ResponseVo findOrderDetailByOrderCode(String orderCode) {
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "查询失败!");
        /**
         * 1.判断skucode是否有值
         */

            if(StringUtils.isEmpty(orderCode)){
                responseVo.setMsg("输入值部位为空!");
                return   responseVo;
            }
            List<OrderDetails> orderDetail=orderMapper.findOrderDetailByOrderCode(orderCode);
        //图片处理
        for (int i = 0; i <orderDetail.size() ; i++) {

            OrderDetails orderDetails=orderDetail.get(i);
            List<String>  images=goodsMapper.findImagesBySkuCode(orderDetails.getSkuCode());
            if(images==null ||images.size()==0){
                orderDetails.setImages(new ArrayList<>());
            }else {
                orderDetails.setImages(images);
            }
        }
            if(orderDetail!=null){
                responseVo.setMsg("查询成功!");
                responseVo.setSuccess(true);
                responseVo.setCode(ErrorCode.SUCCESS);
                responseVo.setData(orderDetail);
                 return   responseVo;
            }

        return responseVo;
    }

    /**
     * App查询订单
     * @param
     * @return
     */
    @Transactional
    @Override
    public ResponseVo appFindOrderDetails(String pageNum, String pageSize, String loginAccount) {
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "查询失败!");
        /**
         * 1.登录校验
         * 2.产寻所有信息
         * 3.获取并检验skuCode
         * 2.根据订单编号查询订单
         *
         */
        Integer id=null;
        Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(loginAccount);
        if (customerByRedis != null) {
            //Redis里有数据的情况
         id=customerByRedis.getId();
        } else {
            //没有数据的情况，提示登陸
            responseVo.setMsg("请先登录后!");
            return responseVo;
        }
        //        分页操作
        if (!StringUtils.isEmpty(pageNum) && !StringUtils.isEmpty(pageSize)) {
            PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        }
         Order order=new Order();
        order.setOrderUserId(id);
        //查询该用户名下的所有订单
       List<Order> list=orderMapper.listOrder(order);

            for (int j = 0; j <list.size() ; j++) {
               Order order1=list.get(j);
               String orderCode=order1.getOrderCode();
               //将同一订单编码的商品放在一起
           List<OrderDetails>   orderDetailsList=orderMapper.appFindOrderDetails(orderCode);
               //图片处理
                for (int i = 0; i <orderDetailsList.size() ; i++) {
                    OrderDetails orderDetails=orderDetailsList.get(i);
                    List<String>  images=goodsMapper.findImagesBySkuCode(orderDetails.getSkuCode());
                    if(images==null ||images.size()==0){
                        orderDetails.setImages(new ArrayList<>());
                    }else {
                        orderDetails.setImages(images);
                    }
                }
                order1.setOrderDetails(orderDetailsList);

            }
            responseVo.setMsg("查询成功!");
            responseVo.setCode(ErrorCode.SUCCESS);
            responseVo.setSuccess(true);
         PageInfo<Order> orderPageInfo = new PageInfo<>(list);
        responseVo.setData(orderPageInfo);
         return responseVo;


        }

}

//    public static void main(String[] args) {
//
//        OrderVo vo = new OrderVo("sku1", "bus1", 32, "mei");
//        OrderVo vo1 = new OrderVo("sku1", "bus1", 32, "mei");
//        OrderVo vo2 = new OrderVo("sku1", "bus2", 32, "mei");
//        OrderVo vo3 = new OrderVo("sku1", "bus3", 32, "mei");
//
//          List<OrderVo> orderVos = Arrays.asList(vo, vo1, vo2, vo3);
//
//         HashSet hashSet = new HashSet();
//        for (int i = 0; i < orderVos.size(); i++) {
//            hashSet.add(orderVos.get(i).getBusinessCode());
//        }
//        Iterator iterator = hashSet.iterator();
//        Map<String,List<OrderVo>> hashMap = new HashMap();   // 存放 商家和对应的商品集合  key：商家code，value :商家对应的商品集合
//
//        while (iterator.hasNext()){
//            String next = (String) iterator.next(); // 商家code
//            List<OrderVo> list = new ArrayList<OrderVo>();//商家对应的商品集合
//            for (int i = 0; i < orderVos.size(); i++) {
//                String businessCode = orderVos.get(i).getBusinessCode();
//                if(next.equals(businessCode)){
//                  //
//                    list.add( orderVos.get(i));
//                }
//            }
//            hashMap.put(next,list);
//        }
//
//        System.out.println(hashMap);
//    }
       /* List<OrderVo> orderVos = new ArrayList<>();
        orderVos.add(vo);
        orderVos.add(vo1);
        orderVos.add(vo2);
        orderVos.add(vo3);
        Map<String, List<OrderVo>> map = new HashMap<>();
        for (int i = 0; i < orderVos.size(); i++) {
            OrderVo orderVo = orderVos.get(i);

            if (map.get(orderVo.getBusinessCode()) == null) {
                List<OrderVo> list = new ArrayList<>();
                list.add(orderVo);
                map.put(orderVo.getBusinessCode(), list);
            } else {
                map.get(orderVo.getBusinessCode()).add(orderVo);
            }
        }
        System.out.println(map);
    }*/
