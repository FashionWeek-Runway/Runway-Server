package com.example.runway.service.util;

import com.example.runway.dto.store.StoreRes;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService{

    @Override
    public List<StoreRes.StoreBlog> getStoreBlog(String storeName) {
        return connectUrl(storeName);
    }


    public List<StoreRes.StoreBlog> connectUrl(String storeName) {
        String url="https://search.naver.com/search.naver?where=view&sm=tab_jum&query="+storeName+"&nso=&where=blog&sm=tab_opt";
        Connection conn = Jsoup.connect(url);

        Document document = null;

        try {
            document = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getDataList(document);
    }

    private List<StoreRes.StoreBlog> getDataList(Document document) {
        List<StoreRes.StoreBlog> storeBlogList = new ArrayList<>();
        Elements selects = document.select("li.bx._svp_item");	//⭐⭐⭐
        System.out.println("div 갯수: "+selects.size());
        for (int i=0;i<5;i++) {

            String img=selects.get(i).select("span.thumb_count").text();

            if(img.length()!=0) {
                StoreRes.StoreBlog storeBlog = StoreRes.StoreBlog.builder()
                        .imgUrl(selects.get(i).select("img.thumb.api_get").attr("src"))
                        .imgCnt(Integer.parseInt(img))
                        .title(selects.get(i).select("a.api_txt_lines.total_tit._cross_trigger").text())
                        .webUrl(selects.get(i).select("a.api_txt_lines.total_tit._cross_trigger").attr("href"))
                        .content(selects.get(i).select("div.api_txt_lines.dsc_txt").text()).build();
                storeBlogList.add(storeBlog);
            }

        }

        return storeBlogList;
    }


}
