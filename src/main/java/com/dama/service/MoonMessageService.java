package com.dama.service;

import java.util.List;

import com.dama.dto.MoonMessageCreateRequest;
import com.dama.dto.MoonMessageResponse;

public interface MoonMessageService {

	 MoonMessageResponse createMessage(String shareToken, MoonMessageCreateRequest request);
	 
	 List<MoonMessageResponse> getMessages(Long moonId);
}
