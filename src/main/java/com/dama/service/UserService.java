package com.dama.service;

import com.dama.dto.UserMeResponse;
import com.dama.entity.enums.SocialProvider;

public interface UserService {

    UserMeResponse getMyInfo(
            SocialProvider provider,
            String providerId
    );
}