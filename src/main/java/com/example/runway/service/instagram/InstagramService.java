package com.example.runway.service.instagram;

import com.example.runway.dto.PageResponse;
import com.example.runway.dto.admin.AdminReq;
import com.example.runway.dto.home.HomeRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InstagramService {
    void postFeed(AdminReq.PostFeed postFeed, List<MultipartFile> feedImg);

    PageResponse<List<HomeRes.InstaFeed>> getInstaFeed(int size, int page);
}
