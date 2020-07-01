package com.neusoft.bookstore.goods.service;

import com.neusoft.bookstore.goods.model.Goods;
import com.neusoft.bookstore.util.ResponseVo;
import org.springframework.web.multipart.MultipartFile;

public interface GoodsService {
    ResponseVo listBusinss();

    ResponseVo uploadImage(MultipartFile file);

    ResponseVo addGoods(Goods goods);

    ResponseVo listGoods(Goods goods);

    ResponseVo findGoodsBySkuCode(String skuCode);

    ResponseVo findBusinessByCode(String businessCode);

    ResponseVo updateGoodsInfo(Goods goods);

    ResponseVo deleteGoods(String skuCode, String loginAccount);

    ResponseVo updateGoodStatus(String skuCode, String loginAccount, String status);
}
