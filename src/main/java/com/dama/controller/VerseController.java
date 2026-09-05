package com.dama.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dama.dto.VerseResponse;
import com.dama.service.VerseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/verses")
@RequiredArgsConstructor
public class VerseController {

    private final VerseService verseService;


    // =========================================================
    // 전체 말씀 조회
    // =========================================================

    @GetMapping
    public ResponseEntity<List<VerseResponse>> getVerses() {

        List<VerseResponse> response =
                verseService.getVerses();

        return ResponseEntity.ok(response);
    }
}