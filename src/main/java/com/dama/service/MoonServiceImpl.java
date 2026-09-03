package com.dama.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.dama.dto.MoonCreateRequest;
import com.dama.dto.MoonResponse;
import com.dama.entity.Moon;
import com.dama.entity.User;
import com.dama.entity.enums.SocialProvider;
import com.dama.repository.MoonMessageRepository;
import com.dama.repository.MoonRepository;
import com.dama.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoonServiceImpl implements MoonService {

    private final MoonRepository moonRepository;
    private final UserRepository userRepository;
    private final MoonMessageRepository moonMessageRepository;


    // 내 보름달 생성
    @Override
    @Transactional
    public MoonResponse createMoon(
            SocialProvider provider,
            String providerId,
            MoonCreateRequest request
    ) {

        // 1. 로그인 사용자 조회
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


        // 2. 이미 해당 시즌 보름달이 있는지 확인
        boolean alreadyExists =
                moonRepository
                        .findByUserIdAndSeasonYear(
                                user.getId(),
                                request.getSeasonYear()
                        )
                        .isPresent();

        if (alreadyExists) {
            throw new IllegalStateException(
                    "이미 해당 시즌의 보름달이 존재합니다."
            );
        }


        // 3. Moon 생성
        Moon moon = new Moon(
                user,
                request.getSeasonYear(),
                request.getOwnerName(),
                UUID.randomUUID().toString()
        );


        // 4. DB 저장
        Moon savedMoon =
                moonRepository.save(moon);


        // 5. DTO 반환
        return MoonResponse.from(savedMoon);
    }


 // 공유 링크로 보름달 조회
    @Override
    public MoonResponse getMoonByShareToken(
            String shareToken
    ) {

        // 1. shareToken으로 Moon 조회
        Moon moon = moonRepository
                .findByShareToken(shareToken)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "존재하지 않는 보름달입니다."
                        )
                );


        // 2. 해당 Moon이 받은 말씀 개수 조회
        int receivedCount =
                moonMessageRepository
                        .findByMoon_IdOrderByCreatedAtDesc(
                                moon.getId()
                        )
                        .size();


        // 3. Moon 정보 + 받은 말씀 개수 반환
        return MoonResponse.from(
                moon,
                receivedCount
        );
    }


    // 내 시즌 보름달 조회
    @Override
    public MoonResponse getMyMoon(
            SocialProvider provider,
            String providerId,
            Integer seasonYear
    ) {

        // 로그인 사용자 조회
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


        // 해당 사용자의 해당 시즌 Moon 조회
        Moon moon =
                moonRepository
                        .findByUserIdAndSeasonYear(
                                user.getId(),
                                seasonYear
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "해당 시즌의 보름달이 없습니다."
                                )
                        );


        return MoonResponse.from(moon);
    }


    // moonId로 보름달 조회
    @Override
    public MoonResponse getMoon(
            Long moonId
    ) {

        Moon moon =
                moonRepository
                        .findById(moonId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "보름달을 찾을 수 없습니다."
                                )
                        );

        return MoonResponse.from(moon);
    }
}