package com.neusoft.bookstore.statisticsinfo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 煤小二
 * @Date 2020/5/28 18:12
 */
@Mapper
public interface StatisticsInfoMapper {

    int selectCustomer();

    int findSexMan();

    int findSexWomen();

    List<String> cateName();

    List<Integer> findCateGoodsNum(String cateName);

    int orderNum();

    int orderFinishNum();
}
