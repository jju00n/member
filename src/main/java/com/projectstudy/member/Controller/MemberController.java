package com.projectstudy.member.Controller;

import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 주석
    @GetMapping("/test")
    public String test(@RequestBody String name) {
        return name;
    }

    @ResponseBody
    @PostMapping("/join")
    public MemberDto join(@RequestBody MemberDto memberDto) {
        return memberService.saveMember(memberDto);
    }

}
