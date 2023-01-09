package com.projectstudy.member.service;

import com.projectstudy.member.domain.Member;
import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto saveMember(MemberDto memberDto) {

        Member member = Member.builder()
                .userName(memberDto.getUserName())
                .userId(memberDto.getUserId())
                .userPw(memberDto.getUserPw())
        .build();

        memberRepository.save(member);

        return memberDto;
    }
}
