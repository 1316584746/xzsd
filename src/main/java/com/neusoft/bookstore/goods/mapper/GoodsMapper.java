package com.neusoft.bookstore.goods.mapper;

import com.neusoft.bookstore.goods.model.Goods;
import com.neusoft.bookstore.goods.model.GoodsImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {
    List<Map<String, Object>> listBusiness();

    Goods findGoodsByCondition(Goods goods);

    int addGoods(Goods goods);

    void addImages(GoodsImage goodsImage);

    List<Goods> listGoods(Goods goods);

    List<String> findImagesBySkuCode(String skuCode);

    Goods findGoodsBySkuCode(String skuCode);

    Map<String, Object> findBusinessByCode(String businessCode);

    int updateGoodsInfo(Goods goods);

    void deleteGoodsImages(String skuCode);

    int deleteGoods(String skuCode);

    int updateGoodsStatus(@Param("skuCode") String skuCode,@Param("updatedBy") String updatedBy,@Param("status") String status);

    void updateGoodsStoreAndSaNum(Map<String, Object> map);
}
