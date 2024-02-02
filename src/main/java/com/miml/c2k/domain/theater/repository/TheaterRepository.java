package com.miml.c2k.domain.theater.repository;

import com.miml.c2k.domain.theater.Theater;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    @Query(value = "SELECT t FROM Theater t JOIN Screen scr ON t.id = scr.theater.id WHERE scr.id = :screenId")
    Optional<Theater> findByScreenId(Long screenId);
}
