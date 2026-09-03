package com.dama.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dama.entity.MoonMessage;

public interface MoonMessageRepository extends JpaRepository<MoonMessage, Long>{

	List<MoonMessage> findByMoon_IdOrderByCreatedAtDesc(Long moonId);
	
	long countByMoon_Id(Long moonId);
}
