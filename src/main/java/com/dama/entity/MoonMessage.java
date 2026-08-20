package com.dama.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "moon_messages")
public class MoonMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long moonId;
    private String sourceType;
    private Long verseId;
    private String category;
    private String manualReference;
    private String manualText;
    private String personalMessage;
    private String authorName;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
}