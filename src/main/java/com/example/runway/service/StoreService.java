package com.example.runway.service;

import com.example.runway.domain.User;
import com.example.runway.dto.store.StoreRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    StoreRes.getHomeList getMainHome(Long userId);

    List<String> getCategoryList();

    StoreRes.StoreInfo getStoreDetail(User user, Long storeId);

    boolean checkStore(Long storeId);

    List<StoreRes.StoreReview> getStoreReview(Long storeId, Pageable page);
}
