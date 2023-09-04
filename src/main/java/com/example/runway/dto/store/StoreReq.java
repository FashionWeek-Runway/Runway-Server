package com.example.runway.dto.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

public class StoreReq {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StoreReport {
        @ApiModelProperty(notes = "주소가 올바르지 않아요 = 1,\n" +
                "    영업 시간이 올바르지 않아요 = 2,\n" +
                "    전화번호가 올바르지 않아요 = 3),\n" +
                "    인스타그램이 연결되지 않아요 =,4),\n" +
                "    홈페이지가 연결되지 않아요= 5)",example = "[1,2,3]")
        private List<Integer> reportReason;
    }
}
