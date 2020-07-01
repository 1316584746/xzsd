package com.neusoft.bookstore.shoppingcar.service;

import com.neusoft.bookstore.shoppingcar.model.ShoppingCar;
import com.neusoft.bookstore.util.ResponseVo;

public interface ShoppingCarService {
    ResponseVo addShoppingCar(ShoppingCar shoppingCar);


    ResponseVo findGoodsFromCar(Integer userId, Integer pageSize, Integer pageNum);

    ResponseVo deleteGoodsFromCar(ShoppingCar shoppingCar);

}
