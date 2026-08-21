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

	@Override
	public Moon getMoon(Long moonId) {
        // moonId로 DB에서 보름달 조회
        return moonRepository.findById(moonId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 보름달입니다.")
                );
	}

}
