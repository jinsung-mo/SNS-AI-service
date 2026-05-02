package com.snsai.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 로컬 테스트 및 API 서버를 위해 CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/hello").permitAll() // hello API는 누구나 접근 가능
                .anyRequest().authenticated() // 나머지는 인증 필요
            );
            
        return http.build();
    }
}
