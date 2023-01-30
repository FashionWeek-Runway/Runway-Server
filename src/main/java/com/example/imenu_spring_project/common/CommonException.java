package com.example.imenu_spring_project.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonException extends Exception {
    private CommonResponseStatus status;
}
