package com.example.runway.service;

import com.example.runway.dto.store.StoreRes;

import java.util.List;

public interface StoreService {
    StoreRes.getHomeList getMainHome(Long userId);

    List<String> getCategoryList();
}
