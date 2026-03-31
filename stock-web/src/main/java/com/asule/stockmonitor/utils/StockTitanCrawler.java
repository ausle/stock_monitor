package com.asule.stockmonitor.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockTitanCrawler {

    private static final String BASE_URL = "https://www.stocktitan.net";
    private static final String LIVE_URL = BASE_URL + "/news/live.html";

    // 更真实的浏览器Headers
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8";
    private static final String ACCEPT_LANGUAGE = "en-US,en;q=0.9";
    private static final String ACCEPT_ENCODING = "gzip, deflate, br";

    // 保存Cookie
    private static Map<String, String> cookies;

    public static void main(String[] args) throws Exception {

        String targetTitle =
                "SCULLY ROYALTY PROVIDES UPDATE ON ANNUAL GENERAL MEETING";

        List<String> tags = getTags(targetTitle);

        System.out.println(tags);
    }

    /**
     * 先访问首页获取Cookie，再请求目标页面
     */
    public static List<String> getTags(String title) throws Exception {
        // 1. 先访问首页获取Cookie
        Document homeDoc = Jsoup.connect(BASE_URL)
                .userAgent(USER_AGENT)
                .header("Accept", ACCEPT)
                .header("Accept-Language", ACCEPT_LANGUAGE)
                .timeout(10_000)
                .execute()
                .parse();

        // 保存Cookie
        cookies = Jsoup.connect(BASE_URL).execute().cookies();

        // 2. 用Cookie请求live页面
        Document doc = Jsoup.connect(LIVE_URL)
                .userAgent(USER_AGENT)
                .header("Accept", ACCEPT)
                .header("Accept-Language", ACCEPT_LANGUAGE)
                .header("Accept-Encoding", ACCEPT_ENCODING)
                .cookies(cookies)
                .timeout(10_000)
                .get();

        return extractTagsByTitle(doc, title);
    }

    /**
     * 根据新闻标题提取标签数组
     */
    private static List<String> extractTagsByTitle(Document doc, String title) {
        List<String> result = new ArrayList<>();

        // 1. 找到所有新闻标题节点
        Elements titleElements = doc.select("a.feed-link");

        for (Element titleEl : titleElements) {
            if (title.equals(titleEl.text())) {

                // 2. 向上找到整个 news-row
                Element newsRow = titleEl.closest("div.news-row");

                if (newsRow == null) {
                    continue;
                }

                // 3. 在该 news-row 中找 tags
                Elements tagElements = newsRow.select("div[name=tags] span.badge");

                for (Element tag : tagElements) {
                    result.add(tag.text().trim());
                }
                break;
            }
        }
        return result;
    }
}
