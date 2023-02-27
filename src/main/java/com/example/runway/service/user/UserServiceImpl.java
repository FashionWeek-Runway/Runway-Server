package com.example.runway.service.user;

import com.example.runway.convertor.UserConvertor;
import com.example.runway.domain.User;
import com.example.runway.domain.UserCategory;
import com.example.runway.domain.pk.UserCategoryPk;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.home.HomeReq;
import com.example.runway.dto.user.UserReq;
import com.example.runway.dto.user.UserRes;
import com.example.runway.repository.StoreRepository;
import com.example.runway.repository.StoreReviewRepository;
import com.example.runway.repository.UserCategoryRepository;
import com.example.runway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final StoreReviewRepository storeReviewRepository;
    private final StoreRepository storeRepository;
    private final LoginService loginService;

    @Override
    public void postUserLocation(User user, UserReq.UserLocation userLocation) {
        user.updateUserLocation(userLocation);
        userRepository.save(user);
    }

    @Override
    public void postUserCategory(Long userId, HomeReq.PostUserCategory postUserCategory) {

        userCategoryRepository.deleteByIdUserId(userId);
        saveUserCategoryList(userId,postUserCategory.getCategoryList());

    }

    @Override
    public List<UserRes.Review> getMyReview(Long userId) {

        List<UserRes.Review> review=new ArrayList<>();


        List<StoreReviewRepository.GetReviewInfo> reviewResult=storeReviewRepository.GetReviewInfo(userId);

        return review;
    }

    @Override
    public UserRes.UserInfo getMyInfo(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        return UserConvertor.UserInfo(user.get());
    }

    @Override
    public UserRes.PatchUserInfo getUserProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return UserConvertor.UserProfile(user.get());
    }

    @Override
    public PageResponse<List<UserRes.StoreInfo>> getMyBookMarkStore(Long userId, Integer page, Integer size) {
        Pageable pageReq = PageRequest.of(page, size);

        Page<StoreRepository.StoreInfoList> storeResult=storeRepository.getMyBookMarkStore(userId,pageReq);
        List<UserRes.StoreInfo> storeInfo=new ArrayList<>();

        storeResult.forEach(
                result->storeInfo.add(
                        new UserRes.StoreInfo(
                                result.getStoreId(),
                                result.getStoreImg(),
                                Stream.of(result.getStoreCategory().split(",")).collect(Collectors.toList()),
                                result.getStoreName()
                                )
                )
        );

        return new PageResponse<>(storeResult.isLast(),storeInfo);
    }

    public void saveUserCategoryList(Long userId,List<Long> userCategoryList) {

        List<UserCategory> userCategoryArrayList = new ArrayList<>();
        for (Long categoryId : userCategoryList) {
            UserCategory userCategory = UserCategory.builder().id(new UserCategoryPk(userId,categoryId)).build();
            userCategoryArrayList.add(userCategory);
        }



        userCategoryRepository.saveAll(userCategoryArrayList);
    }
}
