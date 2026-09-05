package com.dama.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dama.dto.VerseResponse;
import com.dama.repository.VerseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerseServiceImpl implements VerseService {

    private final VerseRepository verseRepository;


    @Override
    public List<VerseResponse> getVerses() {

        return verseRepository
                .findAll()
                .stream()
                .map(VerseResponse::from)
                .toList();
    }
}