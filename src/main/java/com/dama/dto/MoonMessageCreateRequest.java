package com.dama.dto;

import com.dama.entity.enums.VerseCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoonMessageCreateRequest {

    private String sourceType;
    private Long verseId;
    private VerseCategory category;
    private String manualReference;
    private String manualText;
    private String personalMessage;
    private String authorName;
    private Boolean isAnonymous;
    
}
