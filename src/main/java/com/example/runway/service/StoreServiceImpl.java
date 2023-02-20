package com.example.runway.service;

import com.example.runway.convertor.ReviewConvertor;
import com.example.runway.convertor.StoreConvertor;
import com.example.runway.domain.*;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final KeepRepository keepRepository;
    private final StoreImgRepository storeImgRepository;
    private final StoreReviewRepository storeReviewRepository;
    private final AwsS3Service awsS3Service;
    private final OwnerBoardRepository ownerBoardRepository;

    public StoreRes.HomeList getMainHome(Long userId) {

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
    public PageResponse<List<StoreRes.StoreReview>> getStoreReview(Long storeId, int page, int size) {

        List<StoreRes.StoreReview> storeReviewList=new ArrayList<>();

        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreReview> storeReview = storeReviewRepository.findByStoreIdAndStatusOrderByCreatedAtDesc(storeId, true, pageReq);

        for (StoreReview review : storeReview) {
            StoreRes.StoreReview storeReviewDto = StoreConvertor.StoreReviewBuilder(review);
            storeReviewList.add(storeReviewDto);
        }

        return new PageResponse<>(storeReview.isLast(),storeReviewList);
    }

    @Override
    public PageResponse<List<StoreRes.StoreBlog>> getStoreBlog(Long storeId, Integer page, Integer size) {

        return null;
    }

    @Override
    public void postStoreReview(Long storeId, Long userId, MultipartFile multipartFile) throws IOException {
        String imgUrl = awsS3Service.upload(multipartFile,"review");

        StoreReview storeReview = ReviewConvertor.UploadImg(storeId,userId,imgUrl);

        storeReviewRepository.save(storeReview);
    }

    @Override
    public PageResponse<List<StoreRes.StoreBoardList>> getStoreBoard(Long userId, Long storeId, Integer page, Integer size) {
        Pageable pageReq = PageRequest.of(page, size);
        List<StoreRes.StoreBoardList> storeBoard=new ArrayList<>();
        Page<OwnerBoardRepository.StoreBoardList> storeBoardResult=ownerBoardRepository.getStoreBoardList(storeId,pageReq);

        storeBoardResult.forEach(
            result->storeBoard.add(new StoreRes.StoreBoardList(
                    result.getImgUrl(),
                    result.getStoreId(),
                    result.getTitle(),
                    result.getDay()
            ))
        );
        return new PageResponse<>(storeBoardResult.isLast(),storeBoard);
    }

    @Override
    public StoreRes.StoreBoard getStoreBoardById(Long userId, Long boardId) {
        OwnerBoardRepository.StoreBoard result = ownerBoardRepository.getStoreBoard(userId,boardId);
        List<String> categoryList= new ArrayList<>();
        if(result.getImgUrl()!=null){
            categoryList= Stream.of(result.getImgUrl().split(",")).collect(Collectors.toList());
        }
        return StoreConvertor.StoreBoard(result,userId,categoryList);
    }

    private List<String> getStoreImgList(Long storeId) {
        List<StoreImg> storeImg=storeImgRepository.findByStoreIdOrderBySequenceAsc(storeId);



        return storeImg.stream().map(StoreImg::getStoreImg).collect(Collectors.toList());
    }
}
