package com.neusoft.bookstore.cate.mapper;

import com.neusoft.bookstore.cate.model.Cate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CateMapper {
    Cate findCateByParentCateCodeAndName(Cate cate);

    int insertCate(Cate cate);

    Cate findCateByCateCode(String cateCode);

    int updateCateByCode(Cate cate);

    List<Cate> findChildCates(String cateCode);

    int deleteCateByCode(String cateCode);

    List<Cate> listCates();

    List<Cate>  findcateByParentcateCode(String cateCode);

    int findCateByCateCode1(String cateCode);
}
