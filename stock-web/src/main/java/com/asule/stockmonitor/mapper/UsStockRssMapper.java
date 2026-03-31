package com.asule.stockmonitor.mapper;

import com.asule.stockmonitor.bean.UsStockRss;

public interface UsStockRssMapper {
    int deleteByPrimaryKey(String id);

    int insert(UsStockRss record);

    int insertSelective(UsStockRss record);

    UsStockRss selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UsStockRss record);

    int updateByPrimaryKey(UsStockRss record);
}