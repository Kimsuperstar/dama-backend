package com.dama.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.dama.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
            .cors(Customizer.withDefaults())

            .authorizeHttpRequests(auth -> auth

                // 서버 상태 확인
                .requestMatchers(
                        "/api/v1/health"
                ).permitAll()

                // OAuth 관련 주소
                .requestMatchers(
                        "/oauth2/**",
                        "/login/**"
                ).permitAll()

                // 나머지는 로그인 필요
                .anyRequest().authenticated()
            )

            .oauth2Login(oauth -> oauth

                // OAuth 사용자 정보를
                // 우리가 만든 Service에서 처리
                .userInfoEndpoint(userInfo ->
                        userInfo.userService(
                                customOAuth2UserService
                        )
                )

                // 로그인 성공 후 React로 이동
                .defaultSuccessUrl(
                        "http://localhost:5173/?login=success",
                        true
                )
            );

        return http.build();
    }
}