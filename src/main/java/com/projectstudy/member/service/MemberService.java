package com.projectstudy.member.service;

import com.projectstudy.member.domain.Member;
import com.projectstudy.member.dto.ApiResponseDto;
import com.projectstudy.member.dto.JwtToken;
import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.dto.Role;
import com.projectstudy.member.jwt.TokenProvider;
import com.projectstudy.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Map<String, Object> checkIdDuplicate(String id) throws Exception {

        Member member = memberRepository.findByUserId(id);
        Map<String, Object> result = new HashMap<>();

        if(member != null) {
            throw new Exception("이미 사용중인 아이디 입니다.");
        } else {
            result.put("message", "사용 가능한 아이디 입니다.");
        }

        return result;
    }

    public ApiResponseDto saveMember(MemberDto memberDto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String id = memberDto.getUserId();

        Member memberList = memberRepository.findByUserId(id);

        if(memberList == null) {
            Member member;
            if(memberDto.getUserId().equals("admin")) {
                member = Member.builder()
                        .userName(memberDto.getUserName())
                        .userId(memberDto.getUserId())
                        .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                        .role(String.valueOf(Role.ADMIN))
                        .userPhone(memberDto.getUserPhone())
                        .build();

            } else {
                member = Member.builder()
                        .userName(memberDto.getUserName())
                        .userId(memberDto.getUserId())
                        .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                        .role(String.valueOf(Role.USER))
                        .userPhone(memberDto.getUserPhone())
                        .build();

            }
            memberRepository.save(member);

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

    public Boolean findPw(MemberDto memberDto) throws Exception {
        String userId = memberDto.getUserId();
        String userPhone = memberDto.getUserPhone();

        Member member = memberRepository.findByUserIdAndUserPhone(userId, userPhone);

        if(member == null) {
            throw new Exception("회원정보가 없습니다.");
        }

        return true;

    }
    public Boolean changePw(MemberDto memberDto) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String pw = memberDto.getUserPw();

        Member member = memberRepository.findByUserId(memberDto.getUserId());

        if (member != null) {

            member.setUserPw(passwordEncoder.encode(pw));

            memberRepository.updatePw(member.getUserId(), memberDto.getUserPhone(), member.getUserPw());

            return true;
        } else {
            throw new Exception("비밀번호 변경에 실패하였습니다.");
        }
    }

//    public Res login(MemberDto memberDto) {
//
//    }
}
