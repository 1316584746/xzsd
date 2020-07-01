package com.neusoft.bookstore.cate.service;


import com.neusoft.bookstore.cate.model.Cate;
import com.neusoft.bookstore.util.ResponseVo;

public interface CateService
{

    ResponseVo addCate(Cate cate);

    ResponseVo findCateByCode(String cateCode);

    ResponseVo updateCateByCode(Cate cate);

    ResponseVo deleteCateByCode(String cateCode);

    ResponseVo listCateTree();

    ResponseVo findCateByCateCode(String cateCode);
}
