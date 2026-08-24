package com.dama.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dama.entity.User;
import com.dama.entity.enums.SocialProvider;
import com.dama.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserServiceImpl
        implements CustomOAuth2UserService {

    private final UserRepository userRepository;

    private final DefaultOAuth2UserService delegate =
            new DefaultOAuth2UserService();

    @Override
    @Transactional
    public OAuth2User loadUser(
            OAuth2UserRequest userRequest
    ) throws OAuth2AuthenticationException {

        // 1. OAuth 제공자에서 사용자 정보 조회
        OAuth2User oauth2User =
                delegate.loadUser(userRequest);

        // 2. 어떤 소셜 로그인인지 확인
        String registrationId =
                userRequest
                        .getClientRegistration()
                        .getRegistrationId();

        // 현재는 카카오만 지원
        if (!"kakao".equals(registrationId)) {
            throw new OAuth2AuthenticationException(
                    "지원하지 않는 소셜 로그인입니다."
            );
        }

        // 3. 카카오 사용자 정보
        Map<String, Object> attributes =
                oauth2User.getAttributes();

        // 4. 카카오 고유 회원 ID
        String providerId =
                String.valueOf(
                        attributes.get("id")
                );

        // 5. 카카오 닉네임
        String nickname =
                extractKakaoNickname(attributes);

        // 6. 기존 회원 확인
        // 없을 경우에만 신규 회원으로 저장
        userRepository
                .findByProviderAndProviderId(
                        SocialProvider.KAKAO,
                        providerId
                )
                .orElseGet(() -> {

                    User newUser = new User();

                    newUser.setProvider(
                            SocialProvider.KAKAO
                    );

                    newUser.setProviderId(
                            providerId
                    );

                    // 최초 가입 시에만
                    // 카카오 닉네임을 담아 닉네임으로 사용
                    newUser.setNickname(
                            nickname
                    );

                    return userRepository.save(
                            newUser
                    );
                });

        // 7. Spring Security 인증 사용자 반환
        return new DefaultOAuth2User(
                oauth2User.getAuthorities(),
                attributes,
                "id"
        );
    }

    /**
     * 카카오 사용자 정보에서 닉네임 추출
     */
    private String extractKakaoNickname(
            Map<String, Object> attributes
    ) {

        Object accountObject =
                attributes.get("kakao_account");

        if (!(accountObject instanceof Map<?, ?> account)) {
            return "담아 사용자";
        }

        Object profileObject =
                account.get("profile");

        if (!(profileObject instanceof Map<?, ?> profile)) {
            return "담아 사용자";
        }

        Object nickname =
                profile.get("nickname");

        if (nickname == null) {
            return "담아 사용자";
        }

        return nickname.toString();
    }
}