package com.popfilms.popfilmsapi.Repository;

import com.popfilms.popfilmsapi.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByRoleName(String name);
}
