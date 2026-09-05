package com.dama.service;

import java.util.List;

import com.dama.dto.VerseResponse;

public interface VerseService {

	// 전체 말씀 조회
	List<VerseResponse> getVerses();
	
}
