package com.popfilms.popfilmsapi.Repository;

import com.popfilms.popfilmsapi.Entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GenreRepo extends JpaRepository<GenreEntity, Integer> {
    Optional<GenreEntity> findByName(String name);
}
