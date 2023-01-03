package com.example.shop.controller;

import com.example.shop.auth.PrincipalDetails;
import com.example.shop.dto.JoinDto;
import com.example.shop.jwt.JwtProvider;
import com.example.shop.model.Member;
import com.example.shop.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public MemberController(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletResponse response) {

        String result = "logout";

        try {
            jwtProvider.logout(response, principalDetails.getMember().getRefreshToken());
        } catch (Exception e) {
            e.printStackTrace();
            result = "fail";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        return new ResponseEntity<>("profile", HttpStatus.OK);
    }
}
