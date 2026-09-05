package com.dama.service;

import com.dama.dto.UserMeResponse;
import com.dama.entity.enums.SocialProvider;

public interface UserService {

    UserMeResponse getMyInfo(
            SocialProvider provider,
            String providerId
    );
    
    UserMeResponse updateNickname(
            SocialProvider provider,
            String providerId,
            String nickname
    );

    boolean isAdmin(
            SocialProvider provider,
            String providerId
    );
}
