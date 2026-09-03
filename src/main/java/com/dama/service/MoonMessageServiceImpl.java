package com.dama.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.dama.dto.MoonMessageCreateRequest;
import com.dama.dto.MoonMessageResponse;
import com.dama.entity.Moon;
import com.dama.entity.MoonMessage;
import com.dama.entity.Verse;
import com.dama.repository.MoonMessageRepository;
import com.dama.repository.MoonRepository;
import com.dama.repository.VerseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoonMessageServiceImpl implements MoonMessageService {

    private final MoonMessageRepository moonMessageRepository;

    private final MoonRepository moonRepository;

    private final VerseRepository verseRepository;


    @Override
    @Transactional
    public MoonMessageResponse createMessage(
            String shareToken,
            MoonMessageCreateRequest request
    ) {

        // =================================================
        // 1. shareToken으로 Moon 찾기
        // =================================================

        Moon moon = moonRepository
                .findByShareToken(shareToken)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "존재하지 않는 보름달입니다."
                        )
                );


        // =================================================
        // 2. sourceType 확인
        // =================================================

        String sourceType =
                request.getSourceType();


        if (sourceType == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "말씀 입력 방식을 선택해주세요."
            );
        }


        // =================================================
        // 3. Verse 결정
        // =================================================

        Verse verse = null;


        // 기존 말씀을 선택한 경우
        if ("VERSE".equals(sourceType)) {

            if (request.getVerseId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "선택한 말씀 정보가 없습니다."
                );
            }


            verse = verseRepository
                    .findById(
                            request.getVerseId()
                    )
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "존재하지 않는 말씀입니다."
                            )
                    );
        }


        // 직접 입력한 경우
        else if ("MANUAL".equals(sourceType)) {

            if (
                    request.getManualReference() == null
                    || request.getManualReference()
                              .isBlank()
            ) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "말씀 구절을 입력해주세요."
                );
            }


            if (
                    request.getManualText() == null
                    || request.getManualText()
                              .isBlank()
            ) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "말씀 내용을 입력해주세요."
                );
            }
        }


        // VERSE / MANUAL 이외의 값
        else {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "잘못된 말씀 입력 방식입니다."
            );
        }


        // =================================================
        // 4. MoonMessage Entity 생성
        // =================================================

        MoonMessage message = new MoonMessage();
        message.setMoon(moon);
        message.setSourceType(sourceType);

        message.setVerse(verse);

        message.setCategory(
                request.getCategory()
        );

        message.setPersonalMessage(
                request.getPersonalMessage()
        );

        message.setAuthorName(
                request.getAuthorName()
        );

        message.setIsAnonymous(
                request.getIsAnonymous()
        );


        // =================================================
        // 5. 직접 입력한 말씀일 경우에만 저장
        // =================================================

        if ("MANUAL".equals(sourceType)) {

            message.setManualReference(
                    request.getManualReference()
            );

            message.setManualText(
                    request.getManualText()
            );
        }


        // =================================================
        // 6. DB 저장
        // =================================================

        MoonMessage savedMessage = moonMessageRepository.save(message);


        // =================================================
        // 7. Entity → Response DTO
        // =================================================

        return MoonMessageResponse.from(savedMessage);
    }


	@Override
	public List<MoonMessageResponse> getMessages(Long moonId) {
		// Moon 존재 여부 확인
	    moonRepository
	            .findById(moonId)
	            .orElseThrow(() ->
	                    new ResponseStatusException(
	                            HttpStatus.NOT_FOUND,
	                            "존재하지 않는 보름달입니다."
	                    )
	            );

	    return moonMessageRepository
	            .findByMoon_IdOrderByCreatedAtDesc(
	                    moonId
	            )
	            .stream()
	            .map(MoonMessageResponse::from)
	            .toList();
	}


	@Override
	public long getMessageCountByShareToken(String shareToken) {
		Moon moon = moonRepository
	            .findByShareToken(shareToken)
	            .orElseThrow(() ->
	                    new ResponseStatusException(
	                            HttpStatus.NOT_FOUND,
	                            "존재하지 않는 보름달입니다."
	                    )
	            );

	    return moonMessageRepository
	            .countByMoon_Id(
	                    moon.getId()
	            );
	}

}