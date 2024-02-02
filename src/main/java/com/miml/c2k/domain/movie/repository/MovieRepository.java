package com.miml.c2k.domain.movie.repository;

import com.miml.c2k.domain.movie.Movie;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByCode(String code);

    @Query("SELECT distinct m FROM Movie m WHERE m.openDate > :currentDate")
    List<Movie> findMoviesOpeningAfterCurrentDate(LocalDate currentDate);

}
