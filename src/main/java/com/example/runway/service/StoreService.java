package com.example.runway.service;

import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.store.StoreRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StoreService {
    StoreRes.HomeList getMainHome(Long userId);

    List<String> getCategoryList();

    StoreRes.StoreInfo getStoreDetail(User user, Long storeId);

    boolean checkStore(Long storeId);

    PageResponse<List<StoreRes.StoreReview>> getStoreReview(Long storeId, int page,int size);

    PageResponse<List<StoreRes.StoreBlog>> getStoreBlog(Long storeId, Integer page, Integer size);

    void postStoreReview(Long storeId, Long userId, MultipartFile multipartFile) throws IOException;
}
