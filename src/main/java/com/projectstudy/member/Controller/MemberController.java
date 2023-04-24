package com.projectstudy.member.controller;

import com.projectstudy.member.domain.Member;
import com.projectstudy.member.dto.ApiResponseDto;
import com.projectstudy.member.dto.JwtToken;
import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.jwt.JwtFilter;
import com.projectstudy.member.jwt.TokenProvider;
import com.projectstudy.member.repository.MemberRepository;
import com.projectstudy.member.service.MemberService;
import jdk.nashorn.internal.parser.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;

    @PostMapping("/check/id")
    public ResponseEntity<?> checkIdDuplicate(@RequestBody MemberDto memberDto) throws Exception {
        try {
            Map<String, Object> result = memberService.checkIdDuplicate(memberDto.getUserId());
            JSONObject object = new JSONObject();
            object.put("result", true);
            object.put("message", result);

            return ResponseEntity.ok().body(object);
        } catch(Exception e) {
            e.printStackTrace();

            JSONObject object = new JSONObject();
            object.put("result", false);
            object.put("reason", e.getMessage());

            return ResponseEntity.badRequest().body(object);
        }
    }

    @ResponseBody
    @PostMapping("/join")
    public ApiResponseDto join(@RequestBody MemberDto memberDto) {
        return memberService.saveMember(memberDto);
    }

    @PostMapping("/find/id")
    public ApiResponseDto findId(@RequestBody MemberDto memberDto) {
        return memberService.findID(memberDto);
    }

    @PostMapping("/find/pw")
    public ResponseEntity<?> findPw(@RequestBody MemberDto memberDto) {
        JSONObject object = new JSONObject();

        Boolean isPassword = false;
        try {
            isPassword = memberService.findPw(memberDto);

            object.put("result", isPassword);

            return ResponseEntity.ok().body(object);
        } catch (Exception e) {
            e.printStackTrace();
            object.put("result", isPassword);
            object.put("resaon", e.getMessage());

            return ResponseEntity.badRequest().body(object);
        }
    }
    @PutMapping("/change/pw")
    public ResponseEntity<?> changePw(@RequestBody MemberDto memberDto) {
        JSONObject object = new JSONObject();

        Boolean isChange = false;

        try {
            isChange = memberService.changePw(memberDto);

            object.put("result", isChange);
            return ResponseEntity.ok().body(object);
        } catch (Exception e) {
            e.printStackTrace();
            object.put("result", isChange);
            object.put("reason", e.getMessage());

            return ResponseEntity.badRequest().body(object);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody MemberDto memberDto) throws Exception{
        log.info("call login : {}", memberDto);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDto.getUserId(), memberDto.getUserPw());

        // 유저 정보를 조회하여 인증 정보를 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 해당 인증 정보를 현재 실행중인 스레드(Security Context)에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 해당 인증 정보를 기반으로 jwt 토큰을 생성
        String jwt = tokenProvider.createToken(authentication);

        log.info("로그인 토큰 정보 : {}", jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // 생성된 Token을 Response Header에 넣고, Token vo 객체를 이용해 Response Bodyd에도 넣어서 리턴한다
        return new ResponseEntity<>(new JwtToken(jwt), httpHeaders, HttpStatus.OK);

    }


}
