package com.dama.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
	    name = "moons",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            name = "uk_moons_user_year",
	            columnNames = {"user_id", "season_year"}
	        )
	    }
	)
public class Moon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "season_year", nullable = false)
    private Integer seasonYear;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "share_token", nullable = false, unique = true)
    private String shareToken;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public Moon(
            User user,
            Integer seasonYear,
            String ownerName,
            String shareToken
    ) {
        this.user = user;
        this.seasonYear = seasonYear;
        this.ownerName = ownerName;
        this.shareToken = shareToken;
    }
}
