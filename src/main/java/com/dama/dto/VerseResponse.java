package com.dama.dto;

import com.dama.entity.Verse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerseResponse {

    private Long id;

    private String reference;

    private String verse;

    private String translation;

    private String testament;

    private String category;


    public static VerseResponse from(Verse verse) {

        return new VerseResponse(
                verse.getId(),
                verse.getReference(),
                verse.getText(),
                verse.getTranslation(),
                verse.getTestament(),
                verse.getCategory()
        );
    }
}