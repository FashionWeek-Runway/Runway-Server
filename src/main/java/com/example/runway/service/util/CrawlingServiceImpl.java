package com.example.runway.service.util;

import com.example.runway.domain.Store;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.exception.BaseException;
import com.example.runway.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.runway.constants.CommonResponseStatus.NOT_EXIST_STORE;
import static com.example.runway.constants.CommonResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService{
    private final StoreRepository storeRepository;

    @Override
    public List<StoreRes.StoreBlog> getStoreBlog(String storeName, Long storeId) {
        System.out.println(storeId);
        Store store=storeRepository.findByIdAndStatus(storeId,true).orElseThrow(() ->
                new BaseException(NOT_EXIST_STORE));

        String searchContent = store.getSearchContent();
        return connectUrl(searchContent);
    }



    public List<StoreRes.StoreBlog> connectUrl(String searchContent) {
        String url="https://search.naver.com/search.naver?where=view&sm=tab_jum&query="+searchContent+"&nso=&where=blog&sm=tab_opt";


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
        if (selects.isEmpty()){
            return null;
        }
        System.out.println("div 갯수: "+selects.size());

        int cnt = 0;
        for (org.jsoup.nodes.Element select : selects) {

            String img = select.select("span.thumb_count").text();

            if (img.length() != 0) {
                StoreRes.StoreBlog storeBlog = StoreRes.StoreBlog.builder()
                        .imgUrl(select.select("img.thumb.api_get").attr("src"))
                        .imgCnt(Integer.parseInt(img))
                        .title(select.select("a.api_txt_lines.total_tit._cross_trigger").text())
                        .webUrl(select.select("a.api_txt_lines.total_tit._cross_trigger").attr("href"))
                        .content(select.select("div.api_txt_lines.dsc_txt").text()).build();
                storeBlogList.add(storeBlog);
                cnt++;
            }
            if(cnt ==10){
                break;
            }
        }


        return storeBlogList;
    }



}
