package com.neusoft.bookstore.menu.service.impl;

import com.neusoft.bookstore.customer.model.Customer;
import com.neusoft.bookstore.menu.mapper.MenuMapper;
import com.neusoft.bookstore.menu.model.Menu;
import com.neusoft.bookstore.menu.service.MenuService;
import com.neusoft.bookstore.util.BaseTree;
import com.neusoft.bookstore.util.ErrorCode;
import com.neusoft.bookstore.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 煤小二
 * @Date 2020/4/27 11:23
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @Transactional(rollbackFor = Exception.class)//事务管理
    @Override
    public ResponseVo addMenu(Menu menu) {
        /**
         * 1.规定了 一级菜单的父级菜单的code是:0
         * 2.保证一层的菜单名称不一样
         * 3.当某一级有子集菜单时候需要变动当前级别的类型-->目录
         * 4.获取登录人的信息
         */
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "新增失败");
        //4.获取登录人信息
        Customer customerByRedis = (Customer)redisTemplate.opsForValue().get(menu.getLoginAccount());
        if(customerByRedis!=null){
            //Redis里有数据的情况
            menu.setCreatedBy(customerByRedis.getUserAccount());
        }else {
            //没有数据的情况，提示登陸
            responseVo.setMsg("请先登录后操作!");
            return  responseVo;
        }
//        1.规定了 一级菜单的父级菜单的code是:0
        String frontMenuCode = menu.getFrontMenuCode();
        if(StringUtils.isEmpty(frontMenuCode)){
            //没有点击则-->创建一级菜单
            menu.setParentMenuCode("0");
        }else {
            //创建子菜单
            menu.setParentMenuCode(frontMenuCode);
        }
//        2.保证一层的菜单名称不一样
         Menu menuDb=menuMapper.findMenuByParentMenuCodeAndName(menu);
        if(menuDb!=null){
            //名字重复
            responseVo.setMsg("当前级别菜单名已存在!");
            return  responseVo;
        }
//        3.当某一级有子集菜单时候需要变动当前级别的类型-->type值转为目录
        //如果是一级菜单  frontMenuCode 不需要做任何操作  当ParentMenuCode!=0是需要操作
        if(!"0".equals(menu.getParentMenuCode())){
            //更新操作
            //查询父级菜单类型
         Menu  MenuByMenuCode=menuMapper.findMenuByMenuCode(menu.getParentMenuCode());
        if(MenuByMenuCode!=null){
            if(MenuByMenuCode.getType()==2){
                //更新 修改目录  清空menu_url
                menuMapper.updateTypeAndUrlBycode(menu.getParentMenuCode());
            }
        }

        }
        //新增菜单
      int result=menuMapper.insertMenu(menu);
        if(result==1){
            responseVo.setMsg("新增成功");
            responseVo.setCode(ErrorCode.SUCCESS);
            responseVo.setSuccess(true);
            return  responseVo;
        }
        return responseVo;
    }

    /**
     * 菜单树查询
     * @return
     */
    @Override
    public ResponseVo listMenuTree() {
        /**
         *   1.查询所有的菜单(目录+菜单)
         *   2.将菜单信息封装为有层级关系的树信息
         *   3.返回树
         */
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "查询成功!");

//        1.查询所有的菜单(目录+菜单)
         List<Menu> menuList=menuMapper.listMenuTree();
//        2.将菜单信息封装为有层级关系的树信息
        if(null==menuList || menuList.size()==0){
            responseVo.setMsg("未查询到任何信息!");
            return responseVo;
        }

        //创建树  树返回rootTree
        BaseTree rootTreee=new BaseTree();
      //规定根节点树是 0
        String rootNodeId="0";
        initTree(rootTreee,menuList,rootNodeId);
       //3.树返回
        responseVo.setData(rootTreee.getChildNodes());

        return responseVo;
    }

    /**
     * 根据菜单编码菜单详情查询
     * @param menuCode
     * @return
     */
    @Override
    public ResponseVo findMenuByCode(String menuCode) {
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "查询成功");
        //数据校验
        if(StringUtils.isEmpty(menuCode)){
            responseVo.setMsg("菜单编码不能为空!");
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
            return  responseVo;
        }
        //根据菜单编码菜单详情查询
        Menu menu = menuMapper.findMenuByMenuCode(menuCode);
        if(menu==null){
            responseVo.setMsg("未查询到指定菜单信息");
            return responseVo;
        }
        responseVo.setData(menu);
        return responseVo;
    }

    /**
     * 菜单信息修改
     * @param menu
     * @return
     */
    @Override
    public ResponseVo updateMenuByCode(Menu menu) {
        /**
         * 1.检验同一层级 菜单名称不重复
         * 2.修改  判断是否登录
         */
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "修改失败");
//        2.修改  判断是否登录
        //4.获取登录人信息
        Customer customerByRedis = (Customer)redisTemplate.opsForValue().get(menu.getLoginAccount());
        if(customerByRedis!=null){
            //Redis里有数据的情况
            menu.setCreatedBy(customerByRedis.getUserAccount());
        }else {
            //没有数据的情况，提示登陸
            responseVo.setMsg("请先登录后操作!");
            return  responseVo;
        }
//        1.检验同一层级 菜单名称不重复
        Menu menuByDb = menuMapper.findMenuByParentMenuCodeAndName(menu);
        if(menuByDb!=null){
            responseVo.setMsg("当前级别菜单下名称已存在!");
            return responseVo;
        }
        //更新操作
       int result=menuMapper.updateMenuByCode(menu);
        if(result==1){
         responseVo.setMsg("修改成功!");
         responseVo.setCode(ErrorCode.SUCCESS);
         responseVo.setSuccess(true);
         return  responseVo;
     }
        return responseVo;
    }

    /**
     * 删除菜单信息
     * @param menuCode
     * @return
     */
    @Override
    public ResponseVo deleteMenuCode(String menuCode) {
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "删除成功!");
//        数据校验
        if(StringUtils.isEmpty(menuCode)){
            responseVo.setMsg("菜单编码为空!");
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
            return  responseVo;
        }
        //判断该菜单节点下是否有子节点
         List<Menu> menuList=menuMapper.findChildMenus(menuCode);
        if(menuList!=null){
            responseVo.setMsg("当前菜单下有子集,不能删除!");
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
            return  responseVo;
        }
        //执行删除
       int result=menuMapper.deleteMenuByCode(menuCode);
        if(result!=1){
            responseVo.setMsg("删除失败!");
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
            return  responseVo;
        }
        return responseVo;
    }


    private void  initTree(BaseTree rootTreee, List<Menu> menuList, String rootNodeId) {
      //遍历menuList 给每个菜单找位置
        Iterator<Menu> iterator = menuList.iterator();
        while (iterator.hasNext()){
            /**
             * 树找位置:  需要判断menu的menuCode 和 rootNodeId:
             *           1.层级相同
             *           2.是一棵新树的开始
             *       menu的parentMenuCode和 rootNodeId的关系
             */
            Menu menu = iterator.next();
            if(menu.getMenuCode().equals(rootNodeId)){

                //创建根树
                 menuToTree(rootTreee,menu);
            }else if(menu.getParentMenuCode().equals(rootNodeId)){
                //子节点-->创建子树
                BaseTree childTreee = new BaseTree();
                menuToTree(childTreee,menu);
                //需要将子树加入根树
                //****判断根节点的子节点是否已经创建
                if(null!=childTreee.getNodeId()){
                    //创建了 需要将子树加入根树
                    if(rootTreee.getChildNodes()==null){
                        //初始化 根树的子节点
                        ArrayList<BaseTree> list = new ArrayList<>();
                        rootTreee.setChildNodes(list);
                    }
                    //最后往根树中 加入子树
                    rootTreee.getChildNodes().add(childTreee);
                }
                //递归处理
                  initTree(childTreee,menuList,menu.getMenuCode());

            }
        }

    }
//创建根树
    private void menuToTree(BaseTree rootTreee, Menu menu) {
          //节点的id 存 菜单编码
        rootTreee.setNodeId(menu.getMenuCode());
        rootTreee.setNodeName(menu.getMenuName());
        rootTreee.setNodeUrl(menu.getMenuUrl());
        rootTreee.setAttribute(menu);

    }


}
