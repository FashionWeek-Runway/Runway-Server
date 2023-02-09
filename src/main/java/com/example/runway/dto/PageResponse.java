package com.example.runway.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(value = "페이지네이션 API Response")
public class PageResponse<T> {
    @ApiModelProperty(notes = "마지막 페이지 여부", example = "false")
    private final Boolean isLast;

    @ApiModelProperty(notes = "내용", example = "객체가 담깁니다.")
    private final T contents;
}
