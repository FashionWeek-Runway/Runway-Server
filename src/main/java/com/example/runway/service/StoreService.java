package com.example.runway.service;

import com.example.runway.dto.store.StoreRes;

public interface StoreService {
    StoreRes.getHomeList getMainHome(Long userId);
}
