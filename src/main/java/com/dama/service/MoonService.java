package com.dama.service;

import com.dama.dto.MoonCreateRequest;
import com.dama.entity.Moon;

public interface MoonService {
	// 내 보름달 생성
    Moon createMoon(Long userId,  MoonCreateRequest request);

    // 보름달 조회
    Moon getMoon(Long moonId);
}
