package com.miml.c2k.domain.movie.service;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.dto.MovieResponseDto;
import com.miml.c2k.domain.movie.dto.PlayingStatusType;
import com.miml.c2k.domain.schedule.ScheduleRepository;
import com.miml.c2k.domain.admin.dto.MovieAdminResponseDto;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.util.KobisOpenApiUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final ScheduleRepository scheduleRepository;
    private final KobisOpenApiUtil kobisOpenApiUtil;
    private final MovieRepository movieRepository;

    public List<MovieResponseDto> getMoviesPlayingOnScreen(PlayingStatusType playingStatus) {
        List<Movie> moviesAfterCurrentTime = scheduleRepository.findMoviesStartingAfterCurrentTime(
            LocalDateTime.now());
        return moviesAfterCurrentTime.stream().map(
            movie -> MovieResponseDto.create(movie, playingStatus)
        ).toList();
    }

    public void saveBoxOfficeTop10ByTargetDate(String targetDate) {
        kobisOpenApiUtil.saveBoxOfficeTop10ByTargetDate(targetDate);
    }

    public void saveMovieByMovieCode(String movieCode) {
        kobisOpenApiUtil.getMovieInfoByMovieCodeAndSave(movieCode);
    }

    public List<MovieAdminResponseDto> getAllMovies() {
        return movieRepository.findAll().stream().map(MovieAdminResponseDto::create).toList();
    }
}
