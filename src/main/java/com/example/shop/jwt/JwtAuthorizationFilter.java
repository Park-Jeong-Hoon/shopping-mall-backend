package com.example.shop.jwt;

import com.example.shop.auth.PrincipalDetails;
import com.example.shop.model.Member;
import com.example.shop.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String refreshToken = jwtProvider.getRefreshTokenInCookie(request);
        String accessToken = "";
        SecretKey jwtKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtProperties.SECRET));

        if (refreshToken == null) {
            chain.doFilter(request, response);
            return;
        }

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

        Claims claims = null;

        try { // refreshToken 유효성 검증
            jwtProvider.validRefreshToken(refreshToken);
        } catch (Exception e) { // refreshToken 검증 결과에 따른 예외처리
            e.printStackTrace();
            chain.doFilter(request, response);
            return;
        }

        Member member = memberRepository.findByRefreshToken(refreshToken);
        Long memberId = member.getId();

        try {
            if (jwtHeader == null) { // 브라우저 새로고침 고려하여 accessToken 재발급
                accessToken = jwtProvider.createAccessToken(memberId, jwtKey);
                response.addHeader(JwtProperties.HEADER_STRING, accessToken);
            } else { // 그 외의 경우 accessToken 유효성 검증
                accessToken = jwtHeader.replace(JwtProperties.AUTH_TYPE, "");
                claims = Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET).build()
                        .parseClaimsJws(accessToken).getBody();
            }
        } catch (Exception e) { // accessToken 검증 결과에 따른 예외처리
            e.printStackTrace();
            chain.doFilter(request, response);
            return;
        }

        // refreshToken, accessToken 검증 이후 작업
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
