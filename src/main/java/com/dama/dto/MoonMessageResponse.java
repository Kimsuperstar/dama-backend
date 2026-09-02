package com.dama.dto;

import java.time.LocalDateTime;

import com.dama.entity.MoonMessage;
import com.dama.entity.enums.VerseCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoonMessageResponse {

	private Long id;
    private Long moonId;
    private String sourceType;
    private Long verseId;
    private VerseCategory category;
    private String manualReference;
    private String manualText;
    private String personalMessage;
    private String authorName;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    
    public static MoonMessageResponse from(MoonMessage message) {

        return new MoonMessageResponse(
                message.getId(),
                message.getMoon().getId(),
                message.getSourceType(),
                message.getVerse() != null
                        ? message.getVerse().getId()
                        : null,
                message.getCategory(),
                message.getManualReference(),
                message.getManualText(),
                message.getPersonalMessage(),
                message.getAuthorName(),
                message.getIsAnonymous(),
                message.getCreatedAt()
        );
    }
}
