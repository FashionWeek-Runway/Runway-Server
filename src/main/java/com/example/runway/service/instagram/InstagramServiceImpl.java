package com.example.runway.service.instagram;

import com.example.runway.convertor.InstagramConvertor;
import com.example.runway.domain.InstagramFeed;
import com.example.runway.domain.InstagramImg;
import com.example.runway.dto.PageResponse;
import com.example.runway.dto.admin.AdminReq;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.repository.InstagramFeedRepository;
import com.example.runway.repository.InstagramImgRepository;
import com.example.runway.service.util.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstagramServiceImpl implements InstagramService{
    private final AwsS3Service awsS3Service;
    private final InstagramFeedRepository instagramFeedRepository;
    private final InstagramImgRepository instagramImgRepository;
    @Override
    @Transactional
    public void postFeed(AdminReq.PostFeed postFeed, List<MultipartFile> feedImg) {
        System.out.println(postFeed.getLink());
        InstagramFeed instagramFeed = instagramFeedRepository.save(InstagramConvertor.InstagramFeed(postFeed));

        List<String> imgList  =  awsS3Service.uploadImage(feedImg, "insta");

        List<InstagramImg> instagramImg = new ArrayList<>();

        for (String img : imgList) {
            instagramImg.add(InstagramConvertor.InstagramImg(img,instagramFeed.getId()));
        }

        instagramImgRepository.saveAll(instagramImg);

    }

    @Override
    public PageResponse<List<HomeRes.InstaFeed>> getInstaFeed(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);

        Page<InstagramFeed> instaFeed = instagramFeedRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<HomeRes.InstaFeed> instaFeeds = new ArrayList<>();

        instaFeed.getContent().forEach(
                result -> instaFeeds.add(
                        InstagramConvertor.GetInstaFeed(result)
                )
        );

        return new PageResponse<>(instaFeed.isLast(),instaFeeds);
    }
}
