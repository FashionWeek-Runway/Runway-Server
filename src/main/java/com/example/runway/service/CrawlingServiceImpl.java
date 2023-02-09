package com.example.runway.service;

import com.example.runway.dto.store.StoreRes;
import com.example.runway.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        String url="https://search.naver.com/search.naver?where=view&sm=tab_jum&query="+storeName;
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

        for (Element select : selects) {
            StoreRes.StoreBlog storeBlog= StoreRes.StoreBlog.builder().imgUrl(select.select("img.thumb.api_get").attr("src"))
                    .title(select.select("a.api_txt_lines.total_tit._cross_trigger").text())
                    .webUrl(select.select("a.api_txt_lines.total_tit._cross_trigger").attr("href")).build();

            storeBlogList.add(storeBlog);
        }
        return storeBlogList;
    }


}
