package com.example.shop.repository;

import com.example.shop.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
    Member findByRefreshToken(String refreshToken);
    List<Member> findAllByUsername(String username);
}
