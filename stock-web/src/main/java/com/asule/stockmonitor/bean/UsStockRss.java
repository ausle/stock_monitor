package com.asule.stockmonitor.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * us_stock_rss
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsStockRss implements Serializable {
    private String id;

    private String stockCode;

    private String title;

    private String titleZh;

    private String link;

    /**
     * 新闻的发布时间(GMT时间)
     */
    private LocalDateTime pubDateGmt;

    /**
     * 新闻的发布时间(北京时间)
     */
    private LocalDateTime pubDateBj;

    /**
     * 股票标签
     */
    private String tags;
}