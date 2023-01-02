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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    @Builder
    public MemberDto(String userName, String userId, String userPw) {
        this.userName = userName;
        this.userId = userId;
        this.userPw = userPw;
    }

}
