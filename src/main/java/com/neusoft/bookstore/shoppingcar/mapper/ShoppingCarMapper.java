package com.neusoft.bookstore.shoppingcar.mapper;

import com.neusoft.bookstore.shoppingcar.model.ShoppingCar;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCarMapper {
    ShoppingCar findGoodsFromCar(ShoppingCar shoppingCar);

    int addShoppingCar(ShoppingCar shoppingCar);

    int updateShoppingCar(ShoppingCar shoppingCar);

    List<ShoppingCar> listGoodsFromCar(@Param("userId") Integer userId);

    int deleteGoodsFromCar(ShoppingCar shoppingCar);

    void deleteCarById(Integer id);
}
