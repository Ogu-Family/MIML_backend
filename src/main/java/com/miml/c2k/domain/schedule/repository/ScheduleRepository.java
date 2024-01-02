package com.miml.c2k.domain.schedule.repository;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.schedule.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT distinct s.movie FROM Schedule s WHERE s.startTime > :currentTime")
    List<Movie> findMoviesStartingAfterCurrentTime(LocalDateTime currentTime);

    @Query("SELECT count(s) FROM Schedule s WHERE (s.screen.id = :screenId) AND ((s.startTime BETWEEN :startTime AND :endTime) OR (s.endTime BETWEEN :startTime AND :endTime))")
    int countAllByScreenIdBetweenTimeline(Long screenId, LocalDateTime startTime,
            LocalDateTime endTime);

    @Query(value = "SELECT sche FROM Schedule sche "
        + "JOIN FETCH Screen sc ON sc.id = sche.screen.id "
        + "WHERE sc.theater.id = :theaterId AND Date(sche.startTime) = :date AND sche.movie.id = :movieId")
    List<Schedule> findAllByMovieIdAndTheaterIdAndDate(Long movieId, Long theaterId, LocalDate date);

    @Query(value = "SELECT m FROM Movie m JOIN Schedule s ON m.id = s.movie.id WHERE s.id = :scheduleId")
    Optional<Movie> findMovieByScheduleId(Long scheduleId);
}
