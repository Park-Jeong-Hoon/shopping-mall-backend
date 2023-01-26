package com.example.shop.config;

import com.example.shop.jwt.JwtAuthenticationFilter;
import com.example.shop.jwt.JwtAuthorizationFilter;
import com.example.shop.jwt.JwtProvider;
import com.example.shop.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public SecurityConfig(CorsConfig corsConfig, MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.corsConfig = corsConfig;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyFilters())
                .and()
                .authorizeRequests()
                .antMatchers("/api/member/join").permitAll()
                .antMatchers("/api/member/login").permitAll()
                .antMatchers("/api/item/all/**").permitAll()
                .antMatchers("/api/item/{id}").permitAll()
                .anyRequest().authenticated()
                .and().build();
    }

    public class MyFilters extends AbstractHttpConfigurer<MyFilters, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository, jwtProvider));
        }
    }
}
