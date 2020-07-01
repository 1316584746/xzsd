package com.neusoft.bookstore.menu.mapper;

import com.neusoft.bookstore.menu.model.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 煤小二
 * @Date 2020/4/27 11:22
 */
@Mapper
public interface MenuMapper {
    /**
     * 查询菜单名重复
     * @param menu
     * @return
     */
    Menu findMenuByParentMenuCodeAndName(Menu menu);

    /**
     * 查询父级菜单类型
     * @param parentMenuCode
     * @return
     */
    Menu findMenuByMenuCode(String parentMenuCode);

    /**
     * 更新父级的url 和 type值
     * @param parentMenuCode
     */
    void updateTypeAndUrlBycode(String parentMenuCode);

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    int insertMenu(Menu menu);

    /**
     * 查询全部数据
     * @return
     */
    List<Menu> listMenuTree();

    /**
     * 修改菜单信息
     * @param menu
     * @return
     */
    int updateMenuByCode(Menu menu);

    List<Menu> findChildMenus(String menuCode);

    /**
     * 删除菜单
     * @param menuCode
     * @return
     */
    int deleteMenuByCode(String menuCode);
}
