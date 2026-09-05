package com.dama.dto;

import com.dama.entity.User;
import com.dama.entity.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMeResponse {

    private Long id;
    private String nickname;
    private String provider;
    private String role;

    public static UserMeResponse from(User user) {
        return new UserMeResponse(
                user.getId(),
                user.getNickname(),
                user.getProvider().name(),
                (
                        user.getRole() != null
                                ? user.getRole()
                                : UserRole.USER
                ).name()
        );
    }
}
