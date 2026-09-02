package com.dama.dto;

import com.dama.entity.Moon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoonResponse {

    private Long id;

    private String ownerName;

    private Integer seasonYear;

    private String shareToken;

    public static MoonResponse from(Moon moon) {

        return new MoonResponse(
                moon.getId(),
                moon.getOwnerName(),
                moon.getSeasonYear(),
                moon.getShareToken()
        );
    }
}