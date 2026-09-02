package com.dama.service;

import com.dama.dto.MoonCreateRequest;
import com.dama.dto.MoonResponse;
import com.dama.entity.enums.SocialProvider;

public interface MoonService {
	// 내 보름달 생성
    MoonResponse createMoon(SocialProvider provider, String providerId,  MoonCreateRequest request);

    // 공유 링크로 보름달 조회
    MoonResponse getMoonByShareToken(String shareToken);
    
    // 내 보름달 조회
    MoonResponse getMyMoon(SocialProvider provider, String providerId, Integer seasonYear);
    
    // 보름달 ID로 조회
    MoonResponse getMoon(Long moonId);
}
