package com.popfilms.popfilmsapi.Repository;

import com.popfilms.popfilmsapi.Entity.MovieEntity;
import com.popfilms.popfilmsapi.dtos.SearchResultDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepo extends JpaRepository<MovieEntity, Long> {
    @Query(value = "SELECT id FROM movies ORDER BY popfilms_rating DESC LIMIT 30", nativeQuery = true)
    List<Long> findTop30Ids();
    @Query(value = "SELECT id FROM movies ORDER BY RAND() LIMIT 30", nativeQuery = true)
    List<Long> find30RandomIds();

    @Query(value = "SELECT new com.popfilms.popfilmsapi.dtos.SearchResultDto(m.id, m.title, m.releaseYear, m.length) FROM MovieEntity m WHERE m.title LIKE %:query%")
    List<SearchResultDto> getMoviesBySearch(@Param("query") String query, Pageable pageable);

}
