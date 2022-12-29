package com.example.shop.repository;

import com.example.shop.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
    Member findByRefreshToken(String refreshToken);
}
