package com.dama.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dama.dto.MoonCreateRequest;
import com.dama.dto.MoonResponse;
import com.dama.entity.enums.SocialProvider;
import com.dama.service.MoonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/moons")
@RequiredArgsConstructor
public class MoonController {

    private final MoonService moonService;


 // =========================================================
 // 내 보름달 조회
 // =========================================================

 @GetMapping("/me")
 public ResponseEntity<MoonResponse> getMyMoon(
         OAuth2AuthenticationToken authentication,
         @RequestParam("seasonYear") Integer seasonYear
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

     // DB의 providerId는 String
     String providerId =
             kakaoId.toString();

     SocialProvider provider =
             getSocialProvider(registrationId);

     // 기존 Moon 조회
     MoonResponse response =
             moonService.getMyMoon(
                     provider,
                     providerId,
                     seasonYear
             );

     return ResponseEntity.ok(response);
 }


 // =========================================================
 // 내 보름달 생성
 // =========================================================

 @PostMapping
 public ResponseEntity<MoonResponse> createMoon(
         OAuth2AuthenticationToken authentication,
         @RequestBody MoonCreateRequest request
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

     // DB의 providerId는 String
     String providerId =
             kakaoId.toString();

     SocialProvider provider =
             getSocialProvider(registrationId);

     // 새로운 Moon 생성
     MoonResponse response =
             moonService.createMoon(
                     provider,
                     providerId,
                     request
             );

     return ResponseEntity.ok(response);
 }
    
    @GetMapping("/share/{shareToken}")
    public ResponseEntity<MoonResponse> getMoonByShareToken(
            @PathVariable("shareToken") String shareToken
    ) {

        MoonResponse response =
                moonService.getMoonByShareToken(
                        shareToken
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
    
	 // =========================================================
	 // moonId로 보름달 조회
	 // =========================================================
	
	 @GetMapping("/{moonId}")
	 public ResponseEntity<MoonResponse> getMoon(
	         @PathVariable("moonId") Long moonId
	 ) {
	
	     MoonResponse response =
	             moonService.getMoon(moonId);
	
	     return ResponseEntity.ok(response);
	 }
}