package com.dama.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dama.entity.Moon;

public interface MoonRepository extends JpaRepository<Moon, Long>{

	Optional<Moon> findByShareToken(String shareToken);
	
    Optional<Moon> findByUserIdAndSeasonYear(
            Long userId,
            Integer seasonYear
    );
}
