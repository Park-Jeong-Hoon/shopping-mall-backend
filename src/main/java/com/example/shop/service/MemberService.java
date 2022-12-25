package com.example.shop.service;

import com.example.shop.model.Member;
import com.example.shop.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Member member) {

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setJoinDate(LocalDateTime.now());
        member.setRoles("ROLE_USER");
        memberRepository.save(member);
    }
}
