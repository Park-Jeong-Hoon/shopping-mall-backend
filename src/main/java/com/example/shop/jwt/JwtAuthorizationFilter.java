package com.example.shop.jwt;

import com.example.shop.auth.PrincipalDetails;
import com.example.shop.model.Member;
import com.example.shop.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.AUTH_TYPE)) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = jwtHeader.replace(JwtProperties.AUTH_TYPE, "");

        Claims claims = null;

        try {
            claims = Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET).build()
                    .parseClaimsJws(jwtToken).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            chain.doFilter(request, response);
            return;
        }

        Long id = Long.parseLong(String.valueOf(claims.get("id")));

        Member member = memberRepository.findById(id).get();

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
