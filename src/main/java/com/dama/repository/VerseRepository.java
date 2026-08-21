package com.dama.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dama.entity.Verse;

public interface VerseRepository extends JpaRepository<Verse, Long>{

}
