package com.dama.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dama.dto.MoonMessageCreateRequest;
import com.dama.dto.MoonMessageResponse;
import com.dama.entity.enums.SocialProvider;
import com.dama.service.ChuseokOpenPolicy;
import com.dama.service.MoonMessageService;
import com.dama.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/moons")
@RequiredArgsConstructor
public class MoonMessageController {

    private final MoonMessageService
            moonMessageService;

    private final UserService userService;

    private final ChuseokOpenPolicy chuseokOpenPolicy;


    @PostMapping(
            "/share/{shareToken}/messages"
    )
    public ResponseEntity<MoonMessageResponse>
            createMessage(

                    @PathVariable("shareToken")
                    String shareToken,

                    @RequestBody
                    MoonMessageCreateRequest request

            ) {

        MoonMessageResponse response =
                moonMessageService
                        .createMessage(
                                shareToken,
                                request
                        );


        return ResponseEntity.ok(
                response
        );
    }
    
 	// =========================================================
    // 특정 보름달의 받은 말씀 목록 조회
    // =========================================================
    @GetMapping("/{moonId}/messages")
    public ResponseEntity<List<MoonMessageResponse>>
            getMessages(
                    @PathVariable("moonId")
                    Long moonId,

                    @RequestParam(
                            name = "previewAsUser",
                            defaultValue = "false"
                    )
                    boolean previewAsUser,

                    OAuth2AuthenticationToken authentication
            ) {

        boolean canPreviewMessages =
                chuseokOpenPolicy.isOpen()
                        || (isAdmin(authentication) && !previewAsUser);

        List<MoonMessageResponse> response =
                moonMessageService
                        .getMessages(
                                moonId,
                                canPreviewMessages
                        );

        return ResponseEntity.ok(response);
    }

    private boolean isAdmin(
            OAuth2AuthenticationToken authentication
    ) {

        if (authentication == null) {
            return false;
        }

        String registrationId =
                authentication
                        .getAuthorizedClientRegistrationId();

        Object id =
                authentication
                        .getPrincipal()
                        .getAttribute("id");

        if (id == null) {
            return false;
        }

        return userService.isAdmin(
                getSocialProvider(registrationId),
                id.toString()
        );
    }

    private SocialProvider getSocialProvider(
            String registrationId
    ) {

        if ("kakao".equals(registrationId)) {
            return SocialProvider.KAKAO;
        }

        if ("naver".equals(registrationId)) {
            return SocialProvider.NAVER;
        }

        throw new IllegalArgumentException(
                "Unsupported social login provider."
        );
    }
    
    @GetMapping(
            "/share/{shareToken}/messages/count"
    )
    public ResponseEntity<Long> getMessageCount(
            @PathVariable("shareToken")
            String shareToken
    ) {

        long count =
                moonMessageService
                        .getMessageCountByShareToken(
                                shareToken
                        );

        return ResponseEntity.ok(count);
    }
}
