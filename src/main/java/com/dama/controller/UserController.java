package com.dama.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dama.dto.UserMeResponse;
import com.dama.dto.UserNicknameUpdateRequest;
import com.dama.entity.enums.SocialProvider;
import com.dama.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getMyInfo(
            OAuth2AuthenticationToken authentication
    ) {

        // 어떤 소셜 로그인인지 확인
        String registrationId =
                authentication
                        .getAuthorizedClientRegistrationId();

     // 카카오 사용자 고유 ID
        Long kakaoId =
                authentication
                        .getPrincipal()
                        .getAttribute("id");

        // DB의 providerId는 String이므로 변환
        String providerId = kakaoId.toString();

        // kakao -> SocialProvider.KAKAO
        SocialProvider provider =
                getSocialProvider(registrationId);

        // 우리 DB의 User 조회
        UserMeResponse response =
                userService.getMyInfo(
                        provider,
                        providerId
                );

        return ResponseEntity.ok(response);
    }
    // 내 닉네임 변경
    @PatchMapping("/me/nickname")
    public ResponseEntity<UserMeResponse> updateNickname(
            OAuth2AuthenticationToken authentication,
            @RequestBody UserNicknameUpdateRequest request
    ) {

        // 어떤 소셜 로그인인지 확인
        String registrationId =
                authentication
                        .getAuthorizedClientRegistrationId();

        // 카카오 사용자 ID
        Long kakaoId =
                authentication
                        .getPrincipal()
                        .getAttribute("id");

        String providerId =
                kakaoId.toString();

        SocialProvider provider =
                getSocialProvider(registrationId);

        UserMeResponse response =
                userService.updateNickname(
                        provider,
                        providerId,
                        request.getNickname()
                );

        return ResponseEntity.ok(response);
    }

 // OAuth registrationId → SocialProvider 변환
    private SocialProvider getSocialProvider(
            String registrationId
    ) {

        if ("kakao".equals(registrationId)) {
            return SocialProvider.KAKAO;
        }

        if ("naver".equals(registrationId)) {
            return SocialProvider.NAVER;
        }

        throw new IllegalArgumentException(
                "지원하지 않는 소셜 로그인입니다."
        );
    }
}