package com.dama.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dama.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
