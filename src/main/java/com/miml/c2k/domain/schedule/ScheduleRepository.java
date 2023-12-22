package com.miml.c2k.domain.schedule;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.startTime > :currentTime " +
            "GROUP BY s.movie.id HAVING COUNT(s.movie.id) = 1")
    List<Schedule> findMoviesStartingAfterCurrentTime(LocalDateTime currentTime);
}
