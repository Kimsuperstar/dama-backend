package com.dama.service;

import java.time.Clock;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

@Component
public class ChuseokOpenPolicy {

    private static final ZonedDateTime CHUSEOK_OPEN_AT =
            ZonedDateTime.parse("2026-09-25T00:00:00+09:00");

    private final Clock clock;

    public ChuseokOpenPolicy() {
        this.clock = Clock.systemDefaultZone();
    }

    public boolean isOpen() {
        return !ZonedDateTime.now(clock).isBefore(CHUSEOK_OPEN_AT);
    }
}
