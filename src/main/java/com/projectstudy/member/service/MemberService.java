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
                        .userPhone(memberDto.getUserPhone())
                        .build();

                memberRepository.save(member);
            } else {
                Member member = Member.builder()
                        .userName(memberDto.getUserName())
                        .userId(memberDto.getUserId())
                        .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                        .role(String.valueOf(Role.USER))
                        .userPhone(memberDto.getUserPhone())
                        .build();

                memberRepository.save(member);
            }

            return new ApiResponseDto(true, HttpStatus.OK.value(), null);

        } else {
            return new ApiResponseDto(false, HttpStatus.BAD_REQUEST.value(), null);
        }

    }

    public ApiResponseDto findID(MemberDto memberDto) {
        String phone = memberDto.getUserPhone();

        Member member = memberRepository.findMemberByUserPhone(phone);

        Map<String, Object> result = new HashMap<>();

        if(member == null) {
            result.put("message", "회원정보가 존재하지 않습니다.");
            return new ApiResponseDto(false, HttpStatus.NOT_FOUND.value(), result);
        }
        result.put("ID",member.getUserId());
        return new ApiResponseDto(true, HttpStatus.OK.value(), result);
    }

    public ApiResponseDto findPw(MemberDto memberDto) {
        String userId = memberDto.getUserId();
        String userPhone = memberDto.getUserPhone();

        Member member = memberRepository.findByUserIdOrUserPhone(userId, userPhone);

        Map<String, Object> result = new HashMap<>();

        if(member == null) {
            result.put("message", "회원정보가 존재하지 않습니다.");
            return new ApiResponseDto(false, HttpStatus.NOT_FOUND.value(), result);
        }

        return new ApiResponseDto(true, HttpStatus.OK.value(), null);

    }
}
