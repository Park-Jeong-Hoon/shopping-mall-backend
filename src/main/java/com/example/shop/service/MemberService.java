package com.example.shop.service;

import com.example.shop.dto.JoinDto;
import com.example.shop.dto.MemberDto;
import com.example.shop.model.Member;
import com.example.shop.repository.MemberRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(JoinDto joinDto) {

        Member member = new Member();
        member.setUsername(joinDto.getUsername());
        member.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        member.setName(joinDto.getName());
        member.setPhone(joinDto.getPhone());
        member.setEmail(joinDto.getEmail());
        member.setJoinDate(LocalDateTime.now());
        member.setRoles("ROLE_USER");
        memberRepository.save(member);
    }

    @Transactional
    @Modifying
    public void profileEdit(Long id, MemberDto memberDto) throws Exception {

        Optional<Member> memberOptional = memberRepository.findById(id);

        if (memberOptional.isEmpty()) {
           throw new Exception("해당 회원 없음");
        }

        Member member = memberOptional.get();
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
    }
}
