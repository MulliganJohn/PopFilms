package com.popfilms.popfilmsapi.Repository;

import com.popfilms.popfilmsapi.Entity.WriterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WriterRepo extends JpaRepository<WriterEntity, Integer> {
    Optional<WriterEntity> findByName(String name);
}
