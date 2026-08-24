package com.dama.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dama.entity.User;
import com.dama.entity.enums.SocialProvider;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderId(
            SocialProvider provider,
            String providerId
    );
    
}
