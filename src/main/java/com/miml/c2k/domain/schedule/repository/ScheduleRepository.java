package com.miml.c2k.domain.schedule.repository;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.schedule.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT distinct s.movie FROM Schedule s WHERE s.startTime > :currentTime")
    List<Movie> findMoviesStartingAfterCurrentTime(LocalDateTime currentTime);

    @Query("SELECT count(s) FROM Schedule s WHERE (s.screen.id = :screenId) AND (s.startTime BETWEEN :startTime AND :endTime) OR (s.endTime BETWEEN :startTime AND :endTime)")
    int countAllByScreenBetweenTimeline(Long screenId, LocalDateTime startTime, LocalDateTime endTime);
}
