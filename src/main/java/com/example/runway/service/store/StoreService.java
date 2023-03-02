package com.example.runway.service.store;

import com.example.runway.domain.User;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.store.StoreRes;

import java.util.List;

public interface StoreService {

    StoreRes.StoreInfo getStoreDetail(User user, Long storeId);

    boolean checkStore(Long storeId);

    PageResponse<List<StoreRes.StoreBlog>> getStoreBlog(Long storeId, Integer page, Integer size);

    PageResponse<List<StoreRes.StoreBoardList>> getStoreBoard(Long userId, Long storeId, Integer page, Integer size);

    StoreRes.StoreBoard getStoreBoardById(Long userId, Long boardId);

    boolean existsBookMark(Long id, Long storeId);

    void unCheckBookMark(Long userId, Long storeId);

    void checkBookMark(Long userId, Long storeId);

    boolean existsBookMarkFeed(Long userId, Long feedId);

    void unCheckBookMarkFeed(Long userId, Long feedId);

    void checkBookMarkFeed(Long userId, Long feedId);

    List<HomeRes.StoreInfo> recommendStore(Long userId, Integer type);

    boolean existsBookMarkReview(Long userId, Long reviewId);

    void unCheckBookMarkReview(Long userId, Long reviewId);

    void checkBookMarkReview(Long userId, Long reviewId);
}
