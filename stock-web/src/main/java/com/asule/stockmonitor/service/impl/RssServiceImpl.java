package com.asule.stockmonitor.service.impl;

import com.asule.stockmonitor.bean.USStockMsg;
import com.asule.stockmonitor.bean.UsStockRss;
import com.asule.stockmonitor.service.RssService;
import com.asule.stockmonitor.utils.GMTDateConverter;
import com.asule.stockmonitor.utils.StockTitanCrawler;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class RssServiceImpl implements RssService {

    public static final String RSS_URL = "https://www.stocktitan.net/rss";

    private String getStockTitle(String title) {
        String[] titleArr = title.split("\\|");
        return titleArr[0].trim();
    }

    private String getStockCode(String title) {
        String[] titleArr = title.split("\\|");
        String stockStr = titleArr[titleArr.length - 1];

        String[] stockCodeArr = stockStr.split("Stock News");

        return stockCodeArr[0].trim();
    }

    @Override
    public void displayRss() throws Exception {
        List<USStockMsg> stockMsgList = new ArrayList<>();

        List<SyndEntry> rssList = this.fetchRssReed(RSS_URL);
        if (rssList == null || rssList.isEmpty()) {
            System.out.println("RSS 列表为空");
            return;
        }

        for (SyndEntry entry : rssList) {
            UsStockRss stockNews = new UsStockRss();

            // 获得股票异动信息的标题
            String title = entry.getTitle();
            // 截取股票的新闻标题
            String titleEn = getStockTitle(title);
            stockNews.setTitle(titleEn);

            // 获得股票异动信息的链接地址，链接内容是新闻详情
            stockNews.setLink(entry.getLink());

            // 获得股票异动信息的发布时间（GMT时间和北京时间）
            Date gmtDateTemp = entry.getPublishedDate();
            LocalDateTime gmtDate = GMTDateConverter.convertGmt(gmtDateTemp);
            stockNews.setPubDateGmt(gmtDate);
            stockNews.setPubDateBj(GMTDateConverter.convertGmtToBeijing(gmtDateTemp));

            // 获得股票异动信息的股票代码
            // Wolters Kluwer inaugurates new Pune office, marking the next chapter of its technology growth journey in India | WTKWY Stock News
            // WTKWY为股票代码
            String stockCode = getStockCode(title);
            stockNews.setStockCode(stockCode);

            // 获得股票异动信息的标签
            try {
//                List<String> tagsList = StockTitanCrawler.getTags(titleEn);
            } catch (Exception e) {
//                throw new RuntimeException(e);
                // 此处因为频繁访问页面抓取数据，可能导致429的反爬虫异常，无需处理，tags无所谓，可以直接设为空值
                // HTTP error fetching URL. Status=403, URL=[https://www.stocktitan.net/news/live.html]
                stockNews.setTags("");
            }
            // TODO 因为该网站的反爬虫设置，目前无法进行爬取标签内容。后续还需进行修改
            stockNews.setTags("");

            USStockMsg stockMsg = new USStockMsg();
            BeanUtils.copyProperties(stockNews, stockMsg);
            stockMsg.setPubDateBj(GMTDateConverter.getBeijingTime(gmtDateTemp));

            stockMsgList.add(stockMsg);
        }
    }

    @Override
    public List<SyndEntry> fetchRssReed(String rssUrl) throws Exception {
        URL url = new URL(rssUrl);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(url));
        return feed.getEntries();
    }
}
