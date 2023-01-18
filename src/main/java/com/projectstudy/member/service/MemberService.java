package com.projectstudy.member.service;

import com.projectstudy.member.domain.Member;
import com.projectstudy.member.dto.ApiResponseDto;
import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public HashMap<String, String> checkIdDuplicate(String id) {
        HashMap<String, String> result = new HashMap<>();

        boolean res = memberRepository.existsByUserId(id);

        if(res) {
            result.put("message", "이미 사용중인 아이디입니다.");
            result.put("result", String.valueOf(res));
        } else {
            result.put("message", "사용 가능한 아이디입니다.");
            result.put("result", String.valueOf(res));
        }

        return result;
    }

    public ApiResponseDto saveMember(MemberDto memberDto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        String id = memberDto.getUserId();

        List<Member> memberList = memberRepository.findByUserId(id);

        if(memberList.isEmpty()) {
            Member member = Member.builder()
                    .userName(memberDto.getUserName())
                    .userId(memberDto.getUserId())
                    .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                    .build();

            memberRepository.save(member);

            return new ApiResponseDto("success", HttpStatus.OK.value());

        } else {

            return new ApiResponseDto("fail", HttpStatus.BAD_REQUEST.value());
        }

    }
}
