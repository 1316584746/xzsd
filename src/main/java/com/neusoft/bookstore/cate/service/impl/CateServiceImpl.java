package com.neusoft.bookstore.cate.service.impl;

import com.neusoft.bookstore.cate.mapper.CateMapper;
import com.neusoft.bookstore.cate.model.Cate;
import com.neusoft.bookstore.cate.service.CateService;
import com.neusoft.bookstore.customer.model.Customer;
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

@Service
public class CateServiceImpl implements CateService {

    @Autowired
    private CateMapper cateMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseVo addCate(Cate cate) {
        /**
         * 新增分类
         *
         * @param menu
         * @return
         */


            /**
             * 规定了一级菜单的父级编码是0 ， 自己的父级分类编码是父级分类的分类编码
             * 2.保证同一层的分类名不能重复
             * 4.获取登录人
             * 5.新增
             *
             */

            //定义返回值
            ResponseVo<Object> responseVo = new ResponseVo<>(false, ErrorCode.FAIL, "新增失败");
            //获取登录人
            Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(cate.getLoginAccount());
            if (customerByRedis != null) {
                //redis已经保存
                cate.setCreatedBy(customerByRedis.getUserAccount());
            } else {
                //提示登录
                responseVo.setMsg("请登录后重试");
                return responseVo;
            }
            //1.规定了一级分类的父级分类编码是0 ， 自己的父级分类编码是父级分类的分类编码
            String frontCateCode = cate.getParentCateCode();
            if (StringUtils.isEmpty(frontCateCode)) {
                //没有点击 创建一级菜单
                cate.setParentCateCode("0");
            } else {
                //非一级菜单
                cate.setParentCateCode(frontCateCode);
            }

//        2.保证同一层的菜单名不能重复
            Cate cateByDb = cateMapper.findCateByParentCateCodeAndName(cate);
            if (cateByDb != null) {
                //有重复
                responseVo.setMsg("当前级别对的分类下名称重复");
                return responseVo;
            }
            //新增
            int result = cateMapper.insertCate(cate);
            if (result == 1) {
                responseVo.setMsg("新增成功");
                responseVo.setSuccess(true);
                responseVo.setCode(ErrorCode.SUCCESS);
                return responseVo;
            }
            return responseVo;
        }
    /**
     * 根据分类编码查询分类详情
     * @param cateCode
     * @return
     */
    @Override
    public ResponseVo findCateByCode(String cateCode) {
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "查询成功");
//        数据校验
        if(StringUtils.isEmpty(cateCode)){
            responseVo.setMsg("分类编码不能为空");
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
        }
        //根据菜单编码查询菜单详情
        Cate cate = cateMapper.findCateByCateCode(cateCode);
        if(cate == null){
            responseVo.setMsg("未查询到指定分类信息");
            return responseVo;
        }
        responseVo.setData(cate);
        return responseVo;
    }

    /**
     * 修改分类
     * @param cate
     * @return
     */
    @Override
    public ResponseVo updateCateByCode(Cate cate) {
        /**
         * 1.校验同一层级 分类名称不重复
         * 2.修改（（取登录人信息）判断是否登录）
         */
        //判断是否登录
        ResponseVo responseVo = new ResponseVo(false, ErrorCode.FAIL, "更新失败");
        Customer customerByRedis = (Customer) redisTemplate.opsForValue().get(cate.getLoginAccount());
        if (customerByRedis != null) {
            //redis已经保存
            cate.setUpdatedBy(customerByRedis.getLoginAccount());
        } else {
            //提示登录
            responseVo.setMsg("请登录后重试");
            return responseVo;
        }
        //1.校验从一层级，分类名不能重复
        Cate cateByDb = cateMapper.findCateByParentCateCodeAndName(cate);
        if(cateByDb != null){
            //证明当前层级有重复的菜单名称
            responseVo.setMsg("当前级别的分类下有重复名称");
            return responseVo;
        }
        //更新操作
        int result = cateMapper.updateCateByCode(cate);
        if(result == 1){
            responseVo.setSuccess(true);
            responseVo.setCode(ErrorCode.SUCCESS);
            responseVo.setMsg("修改成功");
            return responseVo;
        }
        return responseVo;
    }

    /**
     * 删除分类信息
     * @param cateCode
     * @return
     */
    @Override
    public ResponseVo deleteCateByCode(String cateCode) {
        /**
         * 1.先校验cateCode是否存在，如果存在 走删除 ，不存在提示异常
         * 2.如果该分类下有分类，我们提示无法删除
         * 3.如果该分类下有商品信息，同样提示无法删除
         *
         */
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "删除成功");
//        数据校验
        if(StringUtils.isEmpty(cateCode)){
            responseVo.setMsg("分类编码不能为空");
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
        }
        //需要判断该节点下是否有子节点
        List<Cate> cateList =cateMapper.findChildCates(cateCode);
        if(cateList != null && cateList.size() != 0){
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
            responseVo.setMsg("当前分类下有子集，无法删除");
            return responseVo;
        }
        //3.如果该分类下有商品信息，同样提示无法删除

        /**
         * 根据分类代码 查询我们的商品表，是否有记录
         */
        //返回的是 根据分类编码查询到的所有属于该分类的商品数量
        int resultNum = cateMapper.findCateByCateCode1(cateCode);
        if(resultNum != 0){
            //证明该分类下 一定存在某些商品
            //证明该分类节点下 一定有子节点
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setMsg("该分类下有商品，无法删除");
            responseVo.setData(false);
            return responseVo;
        }

        //删除 逻辑删除：根据分类编码 跟新我们的商品分类的is_delete这个字段
        int result = cateMapper.deleteCateByCode(cateCode);
        if(result != 1){
            responseVo.setCode(ErrorCode.FAIL);
            responseVo.setSuccess(false);
            responseVo.setMsg("删除失败");
            return responseVo;
        }
        return responseVo;
    }
    /**
     * 分类树查询
     *
     * @return
     */
    @Override
    public ResponseVo listCateTree() {
        /**
         * 1.找出所有的分类（type为分类和目录）
         * 2.要将分类信息 封装成有层级关系的树信息
         *      （分类信息--->树信息）
         * 3.树返回
         */
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "查询成功");


//        1.找出所有的菜单（type为菜单和目录）
        List<Cate> cateList =  cateMapper.listCates();

//        2.要将菜单信息 封装成有层级关系的树信息
        if(null == cateList || cateList.size() == 0){
            responseVo.setMsg("未查询到任何菜单信息");
            return responseVo;
        }
        //先来创建一棵根树
        BaseTree rootTree = new BaseTree();  //树返回 rootTree
        //规定根节点树的id为0
        String rootNodeId = "0";

        initTree(rootTree,cateList,rootNodeId);

        //3.树返回
        responseVo.setData(rootTree.getChildNodes());
        return responseVo;
    }

    /**
     * 级联加载商品分类
     * @param cateCode
     * @return
     */
    @Override
    public ResponseVo findCateByCateCode(String cateCode) {
        /**
         * 级联：既要加载一级分类，又要根据一级分类取加载对应的二级分类
         * 加载 一级分类  cateCode = null 或者 = ‘’ 加载一级分类
         * 加载 子级分类  cateCode ！= null 或者 ！= ‘’ 加载子级分类
         */
        ResponseVo responseVo = new ResponseVo(true, ErrorCode.SUCCESS, "查询成功");
        String parentCode = null;
        if (StringUtils.isEmpty(cateCode)){
//             cateCode = null 或者 = ‘’
//             加载 一级分类
            parentCode = "0";
        }
        else{
//            cateCode ！= null 或者 ！= ‘'
//            加载二级分类
            parentCode = cateCode;
        }
        //去书库查询
        List<Cate> cateList=cateMapper.findcateByParentcateCode(parentCode);
        //todo 要加载分类下得商品
        responseVo.setData(cateList);
        return responseVo;
    }

    private void initTree(BaseTree rootTree, List<Cate> cateList, String rootNodeId) {
        //需要遍历menuList 给里面每一个菜单 找位置
        Iterator<Cate> iterator = cateList.iterator();
        while(iterator.hasNext()){
            /**
             * 找位置:
             *      需要判断menu的menuCode 和 rootNodeId ：
             *              1.层级相同
             *              2.是一颗新树的开始
             *              menu的parentMenuCode 和 rootNodeId的关系：
             */
           Cate cate = iterator.next();
            if(cate.getCateCode().equals(rootNodeId)){
                //创建树
                cateToTree(rootTree,cate);
            }
            else if(cate.getParentCateCode().equals(rootNodeId))
            {
                //子节点
                //创建子树
                BaseTree childTree = new BaseTree();
                cateToTree(childTree,cate);

                //需要将子树 加入 根树
                //需要判断 根节点的子节点是否已经创建
                if(null != childTree.getNodeId()){
                    //需要将子树 加入 根树
                    if(rootTree.getChildNodes() == null ){
                        //初始化 根树的子节点
                        ArrayList<BaseTree> list = new ArrayList<>();
                        rootTree.setChildNodes(list);
                    }

                    //  最后从根树中 加入子级
                    rootTree.getChildNodes().add(childTree);
                }
                //递归处理，当有多个层级的时候就会使用递归处理
                initTree(childTree,cateList,cate.getCateCode());
            }
        }
    }

    private void cateToTree(BaseTree rootTree, Cate cate) {
        //节点的id 存菜单编码
        rootTree.setNodeId(cate.getCateCode());
        rootTree.setNodeName(cate.getCateName());
        rootTree.setAttribute(cate);
    }

}
