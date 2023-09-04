package com.example.runway.convertor;

import com.example.runway.domain.InstagramFeed;
import com.example.runway.domain.InstagramImg;
import com.example.runway.dto.admin.AdminReq;
import com.example.runway.dto.home.HomeRes;

import java.util.stream.Collectors;

public class InstagramConvertor {
    public static InstagramFeed InstagramFeed(AdminReq.PostFeed postFeed) {
        return InstagramFeed.builder()
                .link(postFeed.getLink())
                .storeName(postFeed.getStoreName())
                .build();
    }

    public static InstagramImg InstagramImg(String img, Long id) {
        return InstagramImg.builder()
                .imgUrl(img)
                .feedId(id)
                .build();
    }

    //        return category.stream().map(e-> e.getCategory().getCategory()).collect(Collectors.toList());
    public static HomeRes.InstaFeed GetInstaFeed(InstagramFeed result) {
        return HomeRes.InstaFeed.builder()
                .feedId(result.getId())
                .instaLink(result.getLink())
                .description(result.getDescription())
                .storeName(result.getStoreName())
                .imgList(result.getInstagramImg().stream().map(InstagramImg::getImgUrl).collect(Collectors.toList()))
                .build();
    }
}
