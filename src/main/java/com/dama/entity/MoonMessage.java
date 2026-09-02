package com.dama.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.dama.entity.enums.VerseCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "moon_messages")
@NoArgsConstructor
public class MoonMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 어떤 보름달에 남긴 메시지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "moon_id",
            nullable = false
    )
    private Moon moon;


    // VERSE / MANUAL
    @Column(name = "source_type")
    private String sourceType;


    // 기존 말씀을 선택했을 경우
    // 직접 입력이면 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verse_id")
    private Verse verse;


    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private VerseCategory category;


    @Column(name = "manual_reference")
    private String manualReference;


    @Column(name = "manual_text")
    private String manualText;


    @Column(name = "personal_message")
    private String personalMessage;


    @Column(name = "author_name")
    private String authorName;


    @Column(name = "is_anonymous")
    private Boolean isAnonymous;


    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;
}