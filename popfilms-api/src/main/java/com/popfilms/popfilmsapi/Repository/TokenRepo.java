package com.popfilms.popfilmsapi.Repository;
import com.popfilms.popfilmsapi.Entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<TokenEntity, Integer>{

    boolean existsByTokenValue(String tokenValue);

    Optional<TokenEntity> findByTokenValue(String tokenValue);

    void deleteByTokenValue(String tokenValue);


}
