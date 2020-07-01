package com.neusoft.bookstore.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.bookstore.customer.mapper.CustomerMapper;
import com.neusoft.bookstore.customer.model.Customer;
import com.neusoft.bookstore.customer.service.CustomerService;
import com.neusoft.bookstore.shoppingcar.mapper.ShoppingCarMapper;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.MD5Util;
import com.neusoft.bookstore.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 20141
 * @Date 2020/4/23 11:00
 * @Version 1.0
 */

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    /*
      用户新增
     */
    @Override
    public ResponseVo addCustomer(Customer customer) {
        /**
         * 用户新增
         * 1.  校验前台输入的用户名(账号和手机号在系统中是否唯一)
         * 2.  需要校验是App注册还是pc注册,通过isAdmin字段校验,只需校验isAdmin是否规范(0或则1)
         * 3.  用户密码加密 MD5
         * 4.  还要处理用户输入的金额(类型转换  String-->BigDecimal)
         */
        //检验手机 用户名是否唯一
        ResponseVo responseVo = new ResponseVo(false,ErrorCode.FAIL,"新增失败");
        List<Customer> customerByDb=customerMapper.findCustomerByPhoneAndAccount(customer);
        if(customerByDb.size()!=0){
            //数据不为空
            responseVo.setMsg("注册失败,用户名或手机好已经存在,请重试!");
            return  responseVo;
        }
        //2.校验是app登录还是pc登录  通过isAdmin的值 校验isAdmin是否规范

        Integer isAdmin=customer.getIsAdmin();
        if(!StringUtils.isEmpty(isAdmin)){
            //校验
            if(isAdmin!=0 && isAdmin !=1){
                //错误提示
                responseVo.setMsg("注册失败,用户名或手机好已经存在,请重试!");
                return  responseVo;
            }

        }else {
            responseVo.setMsg("isAdmin不能为空!");
            return  responseVo;

        }
        //3.  用户密码加密 MD5
        String password = customer.getPassword();
        //加密密码
        String inputPassToFormPass = MD5Util.inputPassToFormPass(password);
        //密码覆盖(加密覆盖原始)
        customer.setPassword(inputPassToFormPass);

        //4.  还要处理用户输入的金额(类型转换  String-->BigDecimal)
        BigDecimal score = new BigDecimal(customer.getFrontScore());
        //将封装好的值放入Score中
        customer.setScore(score);


        //5.对创建人赋值
        //从Redis中获取当前登录用户信息
        Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(customer.getLoginAccount());
        if(customerByRedis!=null){
            customer.setUpdatedBy(customerByRedis.getUserAccount());
        }else {
            customer.setUpdatedBy("admin");
        }
        customer.setCreatedBy("admin");
        //数据入库
        int res=customerMapper.addCustomer(customer);
        if(res!=1){
            //失败
            responseVo.setMsg("新增失败!");
            return  responseVo;
        }
        responseVo.setSuccess(true);
        responseVo.setCode(ErrorCode.SUCCESS);
        responseVo.setMsg("新增成功");
        return  responseVo;

    }


    //用户登录
    @Override
    public ResponseVo login(Customer customer){
         /*
    登录：1：pc端登录 APP端登录 前台无声给后台传值 isadmin 0 1
         2：手机号和用户账号：loginAccount
         3:密码：输入是加密前的（要加密后再去匹配） 数据库密码是加密后的
         4：登录的时候用到了redis 因为在redis保存了当前登录的用户信息
     */

        //定义返回值
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "用户名或手机号不存在、密码错误、登录失败！");
        //效验数据非空  账号信息 密码信息
        if (StringUtils.isEmpty(customer.getLoginAccount())) {
            //账号信息为空
            responseVo.setMsg("用户账号或者手机号不能为空！");
            return responseVo;
        }
        if (StringUtils.isEmpty(customer.getPassword())) {
            //用户密码为空
            responseVo.setMsg("用户密码不能为空！");
            return responseVo;
        }

        //密码加密
        //获取输入密码
        String password = customer.getPassword();
        //加密密码
        String inputPassToFormPass = MD5Util.inputPassToFormPass(password);
        //覆盖原始密码
        customer.setPassword(inputPassToFormPass);

        //去数据库匹配
        Customer customerByBb = customerMapper.selectLoginCustomer(customer);
        if (customerByBb != null) {
            //登陆成功,保存用户信息到redis
            responseVo.setMsg("登陆成功");
            responseVo.setSuccess(true);
            responseVo.setCode(ErrorCode.SUCCESS);
            //
            responseVo.setData(customerByBb);
            //保存用户信息到redis
            // 确定 key(用户账号) 和 value(customerByBb)
            redisTemplate.opsForValue().set(customerByBb.getUserAccount(), customerByBb);
            return responseVo;
        }

        return responseVo;
    }

    /*
    用户退出
     */
    @Override
    public ResponseVo loginOut(String userAccount) {
        //定义一个返回值
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "退出失败！");
        //校验 userAccount
        if (StringUtils.isEmpty(userAccount)) {
            responseVo.setMsg("用户信息不完整，退出失败！");
            return responseVo;
        }
        //有值
        //从redis里面取出userAccount对应的用户信息 key value
        Boolean result = redisTemplate.delete(userAccount);
        if (result) {
            //redis key 删除成功
            responseVo.setMsg("退出成功！");
            responseVo.setCode(ErrorCode.SUCCESS);
            responseVo.setSuccess(true);
            return responseVo;
        }
        return responseVo;
    }

    /*
    用户列表查询
 */
    @Override
    public ResponseVo listCustomers(Customer customer) {
        /*
           1:所有的未删除的用户信息
           2：有模糊查询的功能
           3：封装一个分页信息，前端会给后台传值：传当前页、传每页显示的条数
                limit
                   index：(page-1)*size，page：当前页
                      size：每一页显示的条数
         */

        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "查询成功");

        //添加分页插件
        PageHelper.startPage(customer.getPageNum(), customer.getPageSize());

        //查询用户所有信息
        List<Customer> customerList = customerMapper.listCutomers(customer);

        //封装分页返回信息
        PageInfo<Customer> pageInfo = new PageInfo<>(customerList);

        responseVo.setData(pageInfo);
        return responseVo;
    }

    /*
     用户详情
    */
    @Override
    public ResponseVo findCustomerById(Integer id) {

        //定义返回值
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "查询成功");
        if (id == null) {
            responseVo.setSuccess(false);
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setMsg("用户id不能为空");
            return responseVo;
        }
        Customer customer = customerMapper.findCustomerById(id);
        if (customer == null) {
            responseVo.setSuccess(false);
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setMsg("未查询到用户信息");
            return responseVo;
        }
        responseVo.setData(customer);
        return responseVo;
    }

    /*
   用户修改
*/
    @Override
    public ResponseVo updateCustomerById(Customer customer) {

    /*
    1:需要效验前台输入的用户名（用户账号）和手机号在系统中是否唯一，要将自己该条记录排除在外
    2：处理用户输入的金额（类型转化 String->BigDecimal） JSON
    3：修改人
    4：修改
     */

        //定义返回值
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "修改失败！");

        //1:需要效验前台输入的用户名（用户账号）和手机号在系统中是否唯一，要将自己该条记录排除在外
        List<Customer> customerByDb = customerMapper.findCustomerByPhoneAndAccountExOwn(customer);

        if (customerByDb.size() != 0) {
            //数据库有值，页面输入有重复
            responseVo.setMsg("修改失败！用户账号或者手机号已存在，请检查后重试");
            return responseVo;
        }
        //2：处理用户输入的金额（类型转化 String->BigDecimal） JSON
        //金额处理,一般来说，前后台交互都是Json数据格式
        BigDecimal score = new BigDecimal(customer.getFrontScore());
        //赋值customer中的score
        customer.setScore(score);

        //3：对修改人 赋值
        //从redis获取当前登录人的用户信息
        Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(customer.getLoginAccount());
        if (customerByRedis != null) {
            //redis已经保存
            customer.setUpdatedBy(customerByRedis.getUserAccount());
        } else {
            customer.setUpdatedBy("admin");
        }

        //4：修改
        int result = customerMapper.updateCustomerById(customer);
        if (result == 1) {
            responseVo.setMsg("修改成功");
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(true);
            return responseVo;
        }
        return responseVo;
    }

    /*
     根据用户id去删除(逻辑删除)
     用户删除
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseVo deleteCustomerById(Integer id) {
        /*
          直接更新is_delete字段
         */

        //定义返回值
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "删除成功！");

        if (id == 1) {
            responseVo.setSuccess(false);
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setMsg("用户id不能为空");
            return responseVo;
        }
        int result = customerMapper.deleteCustomerById(id);
        if (result != 1) {
            responseVo.setMsg("删除失败");
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
            return responseVo;
        }
        //删除和这个用户相关的购物车记录
        shoppingCarMapper.deleteCarById(id);

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
    @Override
    public ResponseVo updatePwd(String originPwd, String newPwd, Integer userId, String userAccount) {
        /*
          原始密码需要校验 需要加密以后去数据库匹配
         */

        //返回值
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "修改失败！");
        if (StringUtils.isEmpty(originPwd) || StringUtils.isEmpty(newPwd) || userId == null || StringUtils.isEmpty(userAccount)) {
            //其中有空,返回
            responseVo.setMsg("密码或者账号值不完整");
            return responseVo;
        }
        Customer customerByDb = customerMapper.findCustomerById(userId);
        if (customerByDb == null) {
            responseVo.setMsg("用户信息不存在！");
            return responseVo;
        }
        //原始密码加密
        String inputPassToFormPass = MD5Util.inputPassToFormPass(originPwd);
        String password = customerByDb.getPassword();

        if (!inputPassToFormPass.equals(password)) {
            //原始密码效验不通过
            responseVo.setMsg("原始密码不正确");
            return responseVo;
        }

        //修改
        newPwd = MD5Util.inputPassToFormPass(newPwd);
        Map<Object, Object> map = new HashMap<>();
        map.put("newPwd", newPwd);
        map.put("userId", userId);
        map.put("userAccount", userAccount);

        //从redis获取当前登录人的用户信息
        Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(userAccount);
        if (customerByRedis != null) {
            //redis已经保存
            map.put("userAccount", userAccount);
        } else {
            map.put("userAccount", "admin");
        }

        int result = customerMapper.updatePwdById(map);

        if (result == 1) {
            //更新成功
            responseVo.setMsg("密码修改成功");
            responseVo.setCode(ErrorCode.SUCCESS);
            responseVo.setSuccess(true);
            return responseVo;
        }

        return responseVo;
    }

    /*
      用户积分修改
      newScore:修改后的积分
      userId：修改用户的id
      userAccount：登录人的账号
     */

    @Override
    public ResponseVo updateScore(String frontScore,Integer userId,String userAccount) {
        //定义返回值
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "跟新失败");
        //查询用户详情 是否存在该用户
        Customer customer=customerMapper.findCustomerById(userId);
        if (customer==null){
            responseVo.setMsg("用户不存在不可以修改!");
            return  responseVo;
        }
        if(StringUtils.isEmpty(frontScore)){
            responseVo.setMsg("积分值为空值!");
            return  responseVo;
        }

        // 还要处理用户输入的金额(类型转换  String-->BigDecimal)
        BigDecimal score= new BigDecimal(frontScore);
        //读取用户的积分值
        if(customer.getScore()==null){
            customer.setScore(score);
        }else {
            //不是0
            int newScore=(customer.getScore()).intValue()+Integer.parseInt(frontScore);
            score= new BigDecimal(newScore);
            customer.setScore(score);
        }
        Map<Object, Object> map = new HashMap<>();
        map.put("score",score);
        map.put("userId",userId);
        //从Redis中获取当前登录用户信息
        Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(userAccount);
        if(customerByRedis!=null){
            //Redis里有数据的情况
            map.put("userAccount",userAccount);
        }else{
            //没有数据的情况
            map.put("userAccount","admin");
        }
        int result = customerMapper.updateScoreById(map);
        if(result==1){
            responseVo.setMsg("修改成功!");
            responseVo.setSuccess(true);
            responseVo.setCode(ErrorCode.SUCCESS);
            return  responseVo;
        }

        return responseVo;
    }

}
