package com.dama.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dama.dto.UserMeResponse;
import com.dama.entity.User;
import com.dama.entity.enums.SocialProvider;
import com.dama.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserMeResponse getMyInfo(
            SocialProvider provider,
            String providerId
    ) {

        User user = userRepository
                .findByProviderAndProviderId(
                        provider,
                        providerId
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "로그인 사용자를 찾을 수 없습니다."
                        )
                );

        return UserMeResponse.from(user);
    }
}