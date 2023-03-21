package com.projectstudy.member.service;

import com.projectstudy.member.domain.Member;
import com.projectstudy.member.dto.ApiResponseDto;
import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.dto.Role;
import com.projectstudy.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Map<String, Object> checkIdDuplicate(String id) throws Exception {

        List<Member> member = memberRepository.findByUserId(id);
        Map<String, Object> result = new HashMap<>();

        if(!member.isEmpty()) {
            throw new Exception("이미 사용중인 아이디 입니다.");
        } else {
            result.put("message", "사용 가능한 아이디 입니다.");
        }

        return result;
    }

    public ApiResponseDto saveMember(MemberDto memberDto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String id = memberDto.getUserId();

        List<Member> memberList = memberRepository.findByUserId(id);

        if(memberList.isEmpty()) {
            if(memberDto.getUserId().equals("admin")) {
                Member member = Member.builder()
                        .userName(memberDto.getUserName())
                        .userId(memberDto.getUserId())
                        .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                        .role(String.valueOf(Role.ADMIN))
                        .build();

                memberRepository.save(member);
            } else {
                Member member = Member.builder()
                        .userName(memberDto.getUserName())
                        .userId(memberDto.getUserId())
                        .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                        .role(String.valueOf(Role.USER))
                        .build();

                memberRepository.save(member);
            }

            return new ApiResponseDto("success", HttpStatus.OK.value());

        } else {
            return new ApiResponseDto("fail", HttpStatus.BAD_REQUEST.value());
        }

    }
}
