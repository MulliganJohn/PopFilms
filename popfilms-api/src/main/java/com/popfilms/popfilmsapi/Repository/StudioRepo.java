package com.popfilms.popfilmsapi.Repository;

import com.popfilms.popfilmsapi.Entity.StudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudioRepo extends JpaRepository<StudioEntity, Integer> {
    Optional<StudioEntity> findByName(String name);
}
