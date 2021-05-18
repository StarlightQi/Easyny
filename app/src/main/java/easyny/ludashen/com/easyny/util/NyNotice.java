package easyny.ludashen.com.easyny.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NyNotice {
   static String  url = "http://www.nnxy.cn/xwgg/tzgg.htm";

    public static Map<String, List<String>> notice() throws IOException {
        Map<String,List<String>> map=new HashMap<>();
        List<String> nTitle=new ArrayList<>();
        List<String> nUrl=new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements notices = document.select(".transition250");
        Elements select = notices.select("a.block.fl.time.transition250");
        for(Element context:select){
            nTitle.add(context.attr("title")+"--"+context.text());
            nUrl.add("http://www.nnxy.cn"+context.attr("href").replace("..",""));
        }
        map.put("context",nTitle);
        map.put("url",nUrl);
        return map;
    }

    public static String weather() throws IOException {
        Document document=Jsoup.connect("http://www.tianqi.com/yongningqu/").get();
        Elements select = document.select(".weather_info");
        return select.text();
    }


}