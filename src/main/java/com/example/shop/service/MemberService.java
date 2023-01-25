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
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long join(JoinDto joinDto) {

        List<Member> members = memberRepository.findAllByUsername(joinDto.getUsername());

        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원");
        }

        Member member = new Member();
        member.setUsername(joinDto.getUsername());
        member.setPassword(passwordEncoder.encode(joinDto.getPassword()));
        member.setName(joinDto.getName());
        member.setPhone(joinDto.getPhone());
        member.setEmail(joinDto.getEmail());
        member.setJoinDate(LocalDateTime.now());
        member.setRoles("ROLE_USER");
        member.setAddress(joinDto.getAddress());
        memberRepository.save(member);

        return member.getId();
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
        member.setAddress(memberDto.getAddress());
    }
}
