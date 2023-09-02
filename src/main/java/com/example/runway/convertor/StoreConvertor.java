package com.example.runway.convertor;

import com.example.runway.domain.*;
import com.example.runway.domain.pk.KeepPk;
import com.example.runway.dto.admin.AdminReq;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.dto.map.MapRes;
import com.example.runway.dto.store.Message;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.dto.user.UserRes;
import com.example.runway.repository.OwnerFeedRepository;
import com.example.runway.repository.StoreRepository;
import com.example.runway.repository.StoreReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StoreConvertor {
    public static MapRes.MapMarkerList StoreMapBuilder(Store value) {
        return MapRes.MapMarkerList.builder().storeId(value.getId()).storeName(value.getName()).latitude(value.getLatitude()).longitude(value.getLongitude()).build();
    }

    public static StoreRes.StoreInfo getStoreDetail(StoreRepository.StoreInfo storeResult, boolean checkBookMark, List<String> imgList) {
        return StoreRes.StoreInfo.builder()
                .storeId(storeResult.getStoreId())
                .imgUrlList(imgList)
                .category(Stream.of(storeResult.getCategory().split(",")).collect(Collectors.toList()))
                .storeName(storeResult.getStoreName())
                .address(storeResult.getAddress())
                .storeTime(storeResult.getStoreTime())
                .storePhone(storeResult.getStorePhone())
                .instagram(storeResult.getInstagram())
                .webSite(storeResult.getWebsite())
                .bookmark(checkBookMark)
                .latitude(storeResult.getLatitude())
                .longitude(storeResult.getLongitude()).build();
    }

    public static StoreRes.StoreReview StoreReviewBuilder(StoreReview review) {
        return StoreRes.StoreReview.builder().reviewId(review.getId()).imgUrl(review.getImgUrl()).build();
    }

    public static MapRes.StoreInfo StoreInfo(StoreRepository.StoreInfoList storeResult, List<String> storeCategory) {
        return MapRes.StoreInfo.builder()
                .storeId(storeResult.getStoreId())
                .storeImg(storeResult.getStoreImg())
                .storeName(storeResult.getStoreName())
                .category(storeCategory)
                .latitude(storeResult.getLatitude())
                .longitude(storeResult.getLongitude())
                .build();
    }

    public static MapRes.MapMarkerList PositionBuilder(StoreRepository.StoreInfoList storeResult) {
        return MapRes.MapMarkerList.builder()
                .storeId(storeResult.getStoreId())
                .storeName(storeResult.getStoreName())
                .address(storeResult.getAddress())
                .latitude(storeResult.getLatitude())
                .longitude(storeResult.getLongitude()).build();
    }

    public static StoreRes.StoreBoard StoreBoard(OwnerFeedRepository.StoreBoard result, Long userId, List<String> categoryList) {
        return StoreRes.StoreBoard.builder()
                .myBoard(result.getUserId().equals(userId))
                .bookmark(result.getBookmark())
                .imgUrl(categoryList)
                .feedId(result.getFeedId())
                .title(result.getTitle())
                .day(result.getDay())
                .content(result.getContent())
                .storeId(result.getStoreId())
                .storeName(result.getStoreName())
                .regionInfo(result.getRegionInfo())
                .build();
    }

    public static Keep CheckBookMark(Long userId, Long storeId) {
        KeepPk keepPk = KeepPk.builder().storeId(storeId).userId(userId).build();
        return Keep.builder().id(keepPk).build();
    }

    public static KeepOwnerFeed CheckBookMarkFeed(Long userId, Long feedId) {
        return KeepOwnerFeed.builder().feedId(feedId).userId(userId).build();
    }

    public static StoreRes.ReviewInfo StoreReview(StoreReviewRepository.GetStoreReview result, StoreRes.ReviewResult prevReviewId, StoreRes.ReviewResult nextReviewId, Long userId) {
        return StoreRes.ReviewInfo.builder()
                .reviewId(result.getReviewId())
                .profileImgUrl(result.getProfileImgUrl())
                .nickname(result.getNickname())
                .imgUrl(result.getImgUrl())
                .storeId(result.getStoreId())
                .storeName(result.getStoreName())
                .regionInfo(result.getRegionInfo())
                .reviewInquiry(StoreRes.ReviewInquiry.builder()
                        .prevReviewId(prevReviewId.getReviewId())
                        .prevReviewImgUrl(prevReviewId.getReviewImgUrl())
                        .nextReviewId(nextReviewId.getReviewId())
                        .nextReviewImgUrl(nextReviewId.getReviewImgUrl())
                        .build())
                .bookmark(result.getBookMark())
                .bookmarkCnt(result.getBookmarkCnt())
                .isMy(userId.equals(result.getUserId()))
                .build();
    }

    public static HomeRes.ReviewInfo StoreReviewRecommend(StoreReviewRepository.GetStoreReview result, StoreRes.ReviewResult prevReviewId, StoreRes.ReviewResult nextReviewId, Long userId) {
        return HomeRes.ReviewInfo.builder()
                .reviewId(result.getReviewId())
                .profileImgUrl(result.getProfileImgUrl())
                .nickname(result.getNickname())
                .imgUrl(result.getImgUrl())
                .storeId(result.getStoreId())
                .storeName(result.getStoreName())
                .regionInfo(result.getRegionInfo())
                .bookmarkCnt(result.getBookmarkCnt())
                .regionInfo(result.getRegionInfo())
                .isMy(result.getUserId().equals(userId))
                .bookmark(result.getBookMark())
                .reviewInquiry(UserRes.ReviewInquiry.builder()
                        .prevReviewId(prevReviewId.getReviewId())
                        .prevReviewImgUrl(prevReviewId.getReviewImgUrl())
                        .nextReviewId(nextReviewId.getReviewId())
                        .nextReviewImgUrl(nextReviewId.getReviewImgUrl())
                        .build())
                .build();

    }

    public static StoreInfoReport ReportStoreInfo(Store store, String reportReason) {
        return StoreInfoReport.builder().storeId(store.getId()).reportReason(reportReason).build();
    }

    public static String MakeDiscordMessage(String name, String storeInfoReport) {
        return "🚨쇼룸 "+ name + "에 잘못된 정보 신고가 들어왔어욧🚨\n" + storeInfoReport;
    }

    public static Message MessageBuilder(Store store, String storeInfoReport) {
        List<Message.Embeds> embedList = new ArrayList<>();
        embedList.add(Message.Embeds.builder().title("신고 내용").description(storeInfoReport).build());
        return Message.builder()
                .content("🚨쇼룸 "+ store.getName() + "에 잘못된 정보 신고가 들어왔어요🚨")
                .tts(false)
                .embeds(embedList)
                .build();
    }

    public static Store PostStoreInfo(AdminReq.StoreInfo storeInfo, String imgUrl) {
        return Store.builder()
                .name(storeInfo.getStoreName())
                .website(storeInfo.getWebsite())
                .phoneNumber(storeInfo.getPhoneNumber())
                .time(storeInfo.getTime())
                .instagramLink(storeInfo.getInstagramLink())
                .latitude(storeInfo.getLatitude())
                .longitude(storeInfo.getLongitude())
                .imgUrl(imgUrl)
                .searchContent(storeInfo.getStoreName())
                .address(storeInfo.getAddress()).build();
    }

    public static StoreImg PostStoreImg(Long id, int index, String img) {
        return StoreImg.builder().storeId(id).sequence(index).storeImg(img).build();
    }
}
