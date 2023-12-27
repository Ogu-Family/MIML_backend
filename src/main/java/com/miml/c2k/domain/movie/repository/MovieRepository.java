package com.miml.c2k.domain.movie.repository;

import com.miml.c2k.domain.movie.Movie;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByCode(Long code);

}
