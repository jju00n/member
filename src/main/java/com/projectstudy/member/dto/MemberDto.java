package com.projectstudy.member.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
public class MemberDto {

    @NotNull
    private String userName;
    @NotNull
    private String userId;
    @NotNull
    private String userPw;

    @Builder
    public MemberDto(String userName, String userId, String userPw) {
        this.userName = userName;
        this.userId = userId;
        this.userPw = userPw;
    }

}
