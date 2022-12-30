package com.example.shop.jwt;

import com.example.shop.model.Member;
import com.example.shop.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtProvider {

    private final MemberRepository memberRepository;

    public JwtProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String createAccessToken(Long id, SecretKey jwtKey) { // accessToken 생성

        return Jwts.builder()
                .setClaims(Map.of("id", id))
                .setSubject("access")
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .signWith(jwtKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(SecretKey jwtKey) { // refreshToken 생성

        return Jwts.builder()
                .setSubject("refresh")
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .signWith(jwtKey, SignatureAlgorithm.HS512)
                .compact();
    }

    @Transactional
    public void saveRefreshTokenDB(Long id, String refreshToken) { // 생성한 refreshToken DB에 저장

        Optional<Member> memberOptional = memberRepository.findById(id);

        if (!memberOptional.isEmpty()) {
            Member member = memberOptional.get();
            member.setRefreshToken(refreshToken);
        }
    }

    public void saveRefreshTokenCookie(String refreshToken, HttpServletResponse response) { // refreshToken 쿠키에 저장

        Cookie cookie = new Cookie("refreshToken", refreshToken);

        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public String getRefreshTokenInCookie(HttpServletRequest request) { // 쿠키에서 refreshToken 조회

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public boolean validRefreshToken(String refreshToken) throws Exception { // refreshToken 유효성 검사

        try {
            Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET).build()
                    .parseClaimsJws(refreshToken);
        } catch (Exception e) {
            throw new Exception();
        }
        return true;
    }
}
