package com.projectstudy.member.controller;

import com.projectstudy.member.dto.ApiResponseDto;
import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/check/id")
    public HashMap<String, String> checkIdDuplicate(@RequestBody MemberDto memberDto) {
        return memberService.checkIdDuplicate(memberDto.getUserId());
    }

    @ResponseBody
    @PostMapping("/join")
    public ApiResponseDto join(@RequestBody MemberDto memberDto) {
        return memberService.saveMember(memberDto);
    }

}
