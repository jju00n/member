package com.projectstudy.member.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Builder;
import lombok.Data;

import java.util.List;
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
