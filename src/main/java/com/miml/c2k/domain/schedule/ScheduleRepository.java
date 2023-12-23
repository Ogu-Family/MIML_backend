package com.miml.c2k.domain.schedule;

import com.miml.c2k.domain.movie.Movie;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT distinct s.movie FROM Schedule s WHERE s.startTime > :currentTime")
    List<Movie> findMoviesStartingAfterCurrentTime(LocalDateTime currentTime);
}
