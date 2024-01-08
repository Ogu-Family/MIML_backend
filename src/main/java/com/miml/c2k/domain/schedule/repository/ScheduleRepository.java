package com.miml.c2k.domain.schedule.repository;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.screen.Screen;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT distinct sch.movie FROM Schedule sch WHERE sch.startTime > :currentTime")
    List<Movie> findMoviesStartingAfterCurrentTime(LocalDateTime currentTime);

    @Query("SELECT count(sch) FROM Schedule sch WHERE (sch.screen.id = :screenId) AND ((sch.startTime BETWEEN :startTime AND :endTime) OR (sch.endTime BETWEEN :startTime AND :endTime))")
    int countAllByScreenIdBetweenTimeline(Long screenId, LocalDateTime startTime,
            LocalDateTime endTime);

    @Query(value = "SELECT sch FROM Schedule sch "
        + "JOIN FETCH Screen scr ON scr.id = sch.screen.id "
        + "WHERE scr.theater.id = :theaterId AND Date(sch.startTime) = :date AND sch.movie.id = :movieId")
    List<Schedule> findAllByMovieIdAndTheaterIdAndDate(Long movieId, Long theaterId, LocalDate date);

    @Query(value = "SELECT m FROM Movie m JOIN Schedule sch ON m.id = sch.movie.id WHERE sch.id = :scheduleId")
    Optional<Movie> findMovieByScheduleId(Long scheduleId);

    @Query(value = "SELECT scr FROM Screen scr JOIN Schedule sch ON scr.id = sch.screen.id WHERE sch.id = :scheduleId")
    Optional<Screen> findScreenByScheduleId(Long scheduleId);

    List<Schedule> findAllByMovieId(Long movieId);
}
