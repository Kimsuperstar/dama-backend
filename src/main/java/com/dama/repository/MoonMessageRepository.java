package com.dama.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dama.entity.MoonMessage;

public interface MoonMessageRepository extends JpaRepository<MoonMessage, Long>{

}
