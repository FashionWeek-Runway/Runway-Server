package com.example.imenu_spring_project.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.imenu_spring_project.common.CommonResponseStatus.SUCCESS;


@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@ApiModel(value = "기본 응답")
public class CommonResponse<T> {
    @Schema(description = "성공 유무", required = true, example = "true")
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    @Schema(description = "응답 메시지", required = true, example = "요청에 성공하였습니다.")
    private final String message;
    @Schema(description = "응답코드", required = true, example = "1000")
    private final int code;
    @Schema(description = "응답코드", required = false, example = "응답 결과")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;



    // 요청에 성공한 경우
    public CommonResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    // 요청에 실패한 경우
    public CommonResponse(CommonResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }
}

