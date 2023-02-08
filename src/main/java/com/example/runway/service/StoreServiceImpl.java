package com.example.runway.service;

import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.*;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final KeepRepository keepRepository;
    private final StoreImgRepository storeImgRepository;
    private final StoreReviewRepository storeReviewRepository;

    public StoreRes.getHomeList getMainHome(Long userId) {

        return null;
    }

    public List<String> getCategoryList(){
        List<Category> category = categoryRepository.findAll();
        return category.stream().map(e-> e.getCategory()).collect(Collectors.toList());
    }

    @Override
    public StoreRes.StoreInfo getStoreDetail(User user, Long storeId) {
        StoreRepository.StoreInfo storeResult= storeRepository.getStoreInfo(storeId);

        return StoreConvertor.getStoreDetail(storeResult,keepRepository.existsByUserIdAndStoreId(user.getId(),storeId),getStoreImgList(storeId));
    }

    @Override
    public boolean checkStore(Long storeId) {
        return storeRepository.existsByIdAndStatus(storeId,true);
    }

    @Override
    public List<StoreRes.StoreReview> getStoreReview(Long storeId, Pageable page) {

        List<StoreRes.StoreReview> storeReviewList=new ArrayList<>();
        Slice<StoreReview> storeReview = storeReviewRepository.findByStoreId(storeId,page);

        for (StoreReview review : storeReview) {
            StoreRes.StoreReview storeReviewDto = StoreConvertor.StoreReviewBuilder(review);
            storeReviewList.add(storeReviewDto);
        }
        return storeReviewList;
    }

    private List<String> getStoreImgList(Long storeId) {
        List<StoreImg> storeImg=storeImgRepository.findByStoreId(storeId);

        return storeImg.stream().map(StoreImg::getStoreImg).collect(Collectors.toList());
    }
}
