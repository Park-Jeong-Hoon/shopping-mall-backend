package com.example.shop.controller;

import com.example.shop.dto.JoinDto;
import com.example.shop.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDto joinDto) {

        String result = "success";

        try {
            memberService.save(joinDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = "false";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
