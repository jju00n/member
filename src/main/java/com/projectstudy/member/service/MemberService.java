package com.projectstudy.member.service;

import com.projectstudy.member.domain.Member;
import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member saveMember(MemberDto memberDto) {

        Member member = new Member();

        member.setUserName(memberDto.getUserName());
        member.setUserId(memberDto.getUserId());
        member.setUserPw(memberDto.getUserPw());

        memberRepository.save(member);

        return member;
    }
}
