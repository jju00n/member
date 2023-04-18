package com.projectstudy.member.controller;

import com.projectstudy.member.dto.ApiResponseDto;
import com.projectstudy.member.dto.MemberDto;
import com.projectstudy.member.service.MemberService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

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


}
