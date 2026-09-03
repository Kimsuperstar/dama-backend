package com.dama.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dama.dto.MoonMessageCreateRequest;
import com.dama.dto.MoonMessageResponse;
import com.dama.service.MoonMessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/moons")
@RequiredArgsConstructor
public class MoonMessageController {

    private final MoonMessageService
            moonMessageService;


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
                    Long moonId
            ) {

        List<MoonMessageResponse> response =
                moonMessageService
                        .getMessages(moonId);

        return ResponseEntity.ok(response);
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