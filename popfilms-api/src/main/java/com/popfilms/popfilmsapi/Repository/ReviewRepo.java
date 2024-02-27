package com.popfilms.popfilmsapi.Repository;

import com.popfilms.popfilmsapi.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<ReviewEntity, Long> {

}
