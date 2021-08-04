package com.ren.util;

import com.ren.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ren
 */

@Component
public class HtmlParseUtil {

    public static void main(String[] args) throws IOException {
        new HtmlParseUtil().parseJD("Java").forEach(System.out::println);
    }

    public List<Content> parseJD(String keywords) throws IOException {
        // 获取请求 https://search.jd.com/Search?keyword=java
        // 前提，需要联网


        // 关于这种图片特别多的网站，所有的图片都是延迟加载的
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        // 解析网页(Jsoup返回Document就是浏览器Document对象)
        Document document = Jsoup.parse(new URL(url), 30000);
        // 所有你在js中可以使用的方法，这里都能用
        Element element = document.getElementById("J_goodsList");
        System.out.println(element.html());

        // 获取所有的li元素
        Elements elements = element.getElementsByTag("li");
        // 获取所有的元素内容

        ArrayList<Content> list = new ArrayList<>();
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();

            Content content = new Content();
            content.setTitle(title);
            content.setImg(img);
            content.setPrice(price);
            list.add(content);
        }
        return list;
    }
}
