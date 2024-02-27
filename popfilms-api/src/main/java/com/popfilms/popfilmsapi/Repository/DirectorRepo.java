package com.popfilms.popfilmsapi.Repository;

import com.popfilms.popfilmsapi.Entity.DirectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DirectorRepo extends JpaRepository<DirectorEntity, Integer> {
    Optional<DirectorEntity> findByName(String name);
}
