package com.projectstudy.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ApiResponseDto {

    private String result;
    private Integer code;

    @Builder
    public ApiResponseDto(String result, Integer code) {
        this.result = result;
        this.code = code;
    }

}
