package com.neusoft.bookstore.menu.service;

import com.neusoft.bookstore.menu.model.Menu;
import com.neusoft.bookstore.util.ResponseVo;

/**
 * @author 煤小二
 * @Date 2020/4/27 11:23
 */

public interface MenuService {
    /**
     * 新增菜单
     * @param menu
     * @return
     */
    ResponseVo addMenu(Menu menu);

    /**
     * 菜单树查询
     * @return
     */
    ResponseVo listMenuTree();

    /**
     * 菜单详情查询
     * @param menuCode
     * @return
     */
    ResponseVo findMenuByCode(String menuCode);

    /**
     * 菜单信息修改
     * @param menu
     * @return
     */
    ResponseVo updateMenuByCode(Menu menu);

    /**
     * 删除菜单信息
     * @param menuCode
     * @return
     */
    ResponseVo deleteMenuCode(String menuCode);
}
