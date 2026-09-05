package com.dama.dto;

import java.time.LocalDateTime;

import com.dama.entity.MoonMessage;
import com.dama.entity.Verse;

import lombok.Getter;

@Getter
public class MoonMessageResponse {

    private Long id;

    private Long moonId;

    private String sourceType;

    private Long verseId;

    private String category;


    // PROVIDED 말씀용
    private String verseReference;

    private String verseText;


    // MANUAL 말씀용
    private String manualReference;

    private String manualText;


    private String personalMessage;

    private String authorName;

    private Boolean isAnonymous;

    private LocalDateTime createdAt;

    private Boolean locked;


    // =========================================================
    // 생성자
    // =========================================================

    public MoonMessageResponse(
            Long id,
            Long moonId,
            String sourceType,
            Long verseId,
            String category,
            String verseReference,
            String verseText,
            String manualReference,
            String manualText,
            String personalMessage,
            String authorName,
            Boolean isAnonymous,
            LocalDateTime createdAt,
            Boolean locked
    ) {

        this.id = id;
        this.moonId = moonId;
        this.sourceType = sourceType;
        this.verseId = verseId;
        this.category = category;

        this.verseReference = verseReference;
        this.verseText = verseText;

        this.manualReference = manualReference;
        this.manualText = manualText;

        this.personalMessage = personalMessage;
        this.authorName = authorName;
        this.isAnonymous = isAnonymous;
        this.createdAt = createdAt;
        this.locked = locked;
    }


    // =========================================================
    // Entity -> Response DTO
    // =========================================================

    public static MoonMessageResponse from(
            MoonMessage message
    ) {

        Verse verse =
                message.getVerse();

        return new MoonMessageResponse(

                message.getId(),

                message.getMoon().getId(),

                message.getSourceType(),

                verse != null
                        ? verse.getId()
                        : null,

                // category
                verse != null
                        ? verse.getCategory()
                        : (
                            message.getCategory() != null
                                ? message.getCategory().name()
                                : null
                        ),

                // PROVIDED 말씀 장절
                verse != null
                        ? verse.getReference()
                        : null,

                // PROVIDED 말씀 본문
                verse != null
                        ? verse.getText()
                        : null,

                // MANUAL 말씀 장절
                message.getManualReference(),

                // MANUAL 말씀 본문
                message.getManualText(),

                message.getPersonalMessage(),

                message.getAuthorName(),

                message.getIsAnonymous(),

                message.getCreatedAt(),

                false
        );
    }

    public static MoonMessageResponse lockedFrom(
            MoonMessage message
    ) {

        Verse verse =
                message.getVerse();

        return new MoonMessageResponse(

                message.getId(),

                message.getMoon().getId(),

                message.getSourceType(),

                verse != null
                        ? verse.getId()
                        : null,

                verse != null
                        ? verse.getCategory()
                        : (
                            message.getCategory() != null
                                ? message.getCategory().name()
                                : null
                        ),

                null,

                null,

                null,

                null,

                null,

                null,

                true,

                message.getCreatedAt(),

                true
        );
    }
}
