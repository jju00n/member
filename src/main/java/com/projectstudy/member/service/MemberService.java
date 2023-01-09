package com.projectstudy.member.service;

import com.projectstudy.member.Repository.MemberRepository;
import com.projectstudy.member.domain.Members;
import com.projectstudy.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto saveMember(MemberDto memberDto) {

        Members members = Members.builder()
                .userName(memberDto.getUserName())
                .userId(memberDto.getUserId())
                .userPw(memberDto.getUserPw())
        .build();

        memberRepository.save(members);

//        memberDto.setUserName(memberDto.getUserName());
//        memberDto.setUserId(memberDto.getUserId());
//        memberDto.setUserPw(memberDto.getUserPw());

        return memberDto;
    }
}
