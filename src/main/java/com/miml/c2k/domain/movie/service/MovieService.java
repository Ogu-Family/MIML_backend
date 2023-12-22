package com.miml.c2k.domain.movie.service;

import com.miml.c2k.util.KobisOpenApiUtil;
import com.miml.c2k.domain.movie.dto.MovieResponseDto;
import com.miml.c2k.domain.movie.dto.PlayingStatusType;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.ScheduleRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final ScheduleRepository scheduleRepository;
    private final KobisOpenApiUtil kobisOpenApiUtil;

    public List<MovieResponseDto> getMoviesPlayingOnScreen(PlayingStatusType playingStatus) {
        List<Schedule> schedulesAfterCurrentTime = scheduleRepository.findMoviesStartingAfterCurrentTime(
                LocalDateTime.now());
        return schedulesAfterCurrentTime.stream().map(
                schedule -> schedule.getMovie().toMovieResponseDto(playingStatus)
        ).toList();
    }

    public void saveBoxOfficeTop10ByTargetDate(String targetDate) {
        kobisOpenApiUtil.saveBoxOfficeTop10ByTargetDate(targetDate);
    }

    public void saveMovieByMovieCode(String movieCode) {
        kobisOpenApiUtil.getMovieInfoByMovieCodeAndSave(movieCode);
    }
}
