package com.dama.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

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
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
            		
        		// CORS preflight
                .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**"
                ).permitAll()


                // 서버 상태 확인
                .requestMatchers(
                        "/api/v1/health"
                ).permitAll()
                
                // 공유 보름달 조회 + 친구 말씀 작성
                .requestMatchers(
                        "/api/v1/moons/share/**"
                ).permitAll()

                // OAuth 관련 주소
                .requestMatchers(
                        "/oauth2/**",
                        "/login/**"
                ).permitAll()

                // 나머지는 로그인 필요
                .anyRequest().authenticated()
            )
            
            // 로그인하지 않은 상태에서 API를 호출하면
            // 카카오로 redirect하지 않고 401 반환
            .exceptionHandling(exception -> exception
            	    .defaultAuthenticationEntryPointFor(
            	        new HttpStatusEntryPoint(
            	            HttpStatus.UNAUTHORIZED
            	        ),
            	        request ->
            	            request.getRequestURI()
            	                   .startsWith("/api/")
            	    )
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
            )
        
	        .logout(logout -> logout
	
	                // React가 호출할 로그아웃 주소
	                .logoutUrl("/api/v1/auth/logout")
	
	                // 현재 로그인 세션 제거
	                .invalidateHttpSession(true)
	
	                // Spring Security 인증 정보 제거
	                .clearAuthentication(true)
	
	                // 브라우저의 세션 쿠키 제거
	                .deleteCookies("JSESSIONID")
	
	                // 로그아웃 성공하면 200 반환
	                .logoutSuccessHandler((request, response, authentication) -> {
	                    response.setStatus(200);
	                })
	
	                .permitAll()
	            );

        return http.build();
    }
}