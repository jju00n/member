package com.projectstudy.member.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
public class ApiResponseDto {

    private Boolean result;
    private Integer code;

    private Map data;

    @Builder
    public ApiResponseDto(Boolean result, Integer code, Map data) {
        this.result = result;
        this.code = code;
        this.data = data;
    }

}
