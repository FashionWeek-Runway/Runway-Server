package com.example.runway.service.util;

import com.example.runway.dto.store.StoreRes;

import java.util.List;

public interface CrawlingService {

    List<StoreRes.StoreBlog> getStoreBlog(String storeName, Long storeId);

    void scrapBlog(Long storeId);
}
