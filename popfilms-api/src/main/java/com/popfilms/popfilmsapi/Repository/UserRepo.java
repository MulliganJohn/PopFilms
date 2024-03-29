package com.popfilms.popfilmsapi.Repository;

import com.popfilms.popfilmsapi.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<UserEntity> findByEmailAddress(String emailAddress);

    boolean existsByEmailAddress(String emailAddress);
}
