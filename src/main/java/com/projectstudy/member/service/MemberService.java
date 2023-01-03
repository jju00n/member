package com.projectstudy.member.service;

import com.projectstudy.member.dto.MemberDto;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public MemberDto saveMember(MemberDto memberDto) {


        memberDto.setUserName(memberDto.getUserName());
        memberDto.setUserId(memberDto.getUserId());
        memberDto.setUserPw(memberDto.getUserPw());

        return memberDto;
    }
}
