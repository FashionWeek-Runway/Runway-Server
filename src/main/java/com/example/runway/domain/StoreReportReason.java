package com.example.runway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreReportReason {
    ADDRESS("주소가 올바르지 않아요",1),
    TIME("영업 시간이 올바르지 않아요",2),
    PHONE("전화번호가 올바르지 않아요",3),
    INSTAGRAM("인스타그램이 연결되지 않아요",4),
    HOMEPAGE("홈페이지가 연결되지 않아요",5)
    ;
    private final String value;
    private final int index;

    public static String getValueByIndex(int index) {
        for (StoreReportReason reason : values()) {
            if (reason.index == index) {
                return reason.value;
            }
        }
        return null; // Return null if index is not found
    }

}
