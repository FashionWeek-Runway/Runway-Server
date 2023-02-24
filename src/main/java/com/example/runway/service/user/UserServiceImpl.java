package com.example.runway.service.user;

import com.example.runway.domain.User;
import com.example.runway.domain.UserCategory;
import com.example.runway.dto.home.HomeReq;
import com.example.runway.dto.user.UserReq;
import com.example.runway.repository.UserCategoryRepository;
import com.example.runway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;

    @Override
    public void postUserLocation(User user, UserReq.UserLocation userLocation) {
        user.updateUserLocation(userLocation);
        userRepository.save(user);
    }

    @Override
    public void postUserCategory(Long userId, HomeReq.PostUserCategory postUserCategory) {
        List<UserCategory> userCategoryList = userCategoryRepository.findByUserId(userId);

        List<Integer> oldCategoryList=new ArrayList<>();
        oldCategoryList.add(1);
        oldCategoryList.add(2);
        oldCategoryList.add(3);
        oldCategoryList.add(4);
        oldCategoryList.add(5);
        oldCategoryList.add(6);


        for (UserCategory category : userCategoryList) {
            for (int j = 0; j < postUserCategory.getCategoryList().size(); j++) {
                if (category.getCategoryId().equals(postUserCategory.getCategoryList().get(j))) {
                    category.modifyCategoryStatus(true);
                    userCategoryRepository.save(category);
                    oldCategoryList.remove(Integer.valueOf(String.valueOf(postUserCategory.getCategoryList().get(j))));
                }
            }
        }

        for (Integer integer : oldCategoryList) {
            Optional<UserCategory> userCategory = userCategoryRepository.findByUserIdAndCategoryId(userId, Long.valueOf(integer));
            userCategory.get().modifyCategoryStatus(false);
            userCategoryRepository.save(userCategory.get());
        }

    }
}
