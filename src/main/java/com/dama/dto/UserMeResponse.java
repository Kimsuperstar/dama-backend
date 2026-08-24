package com.dama.dto;

import com.dama.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMeResponse {

    private Long id;
    private String nickname;
    private String provider;

    public static UserMeResponse from(User user) {
        return new UserMeResponse(
                user.getId(),
                user.getNickname(),
                user.getProvider().name()
        );
    }
}