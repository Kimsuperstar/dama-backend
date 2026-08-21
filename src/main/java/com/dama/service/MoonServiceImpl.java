package com.dama.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dama.dto.MoonCreateRequest;
import com.dama.entity.Moon;
import com.dama.repository.MoonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MoonServiceImpl implements MoonService {

	private final MoonRepository moonRepository;
	
	@Override
	public Moon createMoon(Long userId,  MoonCreateRequest request) {

        // Moon 객체 생성
        Moon moon = new Moon();
        // 이 Moon이 누구 것인지 저장
        moon.setUserId(userId);
        // 시즌 저장
        moon.setSeasonYear(request.getSeasonYear());
        // 공유용 토큰 생성
        moon.setShareToken(UUID.randomUUID().toString());

        // DB 저장
        return moonRepository.save(moon);
	}

	// 공유 링크로 보름달 조회
    @Override
    public Moon getMoonByShareToken(String shareToken) {

        return moonRepository.findByShareToken(shareToken)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "존재하지 않는 보름달입니다."
                        )
                );
    }
	
    @Override
    public Moon getMyMoon(Long userId, Integer seasonYear) {

    	// 로그인한 사용자의 시즌별 보름달 조회
        return moonRepository
                .findByUserIdAndSeasonYear(userId, seasonYear)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "해당 시즌의 보름달이 존재하지 않습니다."
                        )
                );
    }

}
