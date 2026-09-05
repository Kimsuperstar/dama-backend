package com.dama.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dama.entity.enums.SocialProvider;
import com.dama.entity.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_users_provider_provider_id",
            columnNames = {"provider", "provider_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialProvider provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'USER'")
    private UserRole role = UserRole.USER;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public User(
    		SocialProvider provider,
            String providerId,
            String nickname
    ) {
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.role = UserRole.USER;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}

