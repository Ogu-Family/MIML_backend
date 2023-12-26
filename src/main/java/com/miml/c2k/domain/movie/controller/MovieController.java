package com.miml.c2k.domain.movie.controller;

import com.miml.c2k.domain.movie.dto.MovieResponseDto;
import com.miml.c2k.domain.movie.dto.PlayingStatusType;
import com.miml.c2k.domain.movie.service.MovieService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/api/v1/movies")
    public List<MovieResponseDto> getMoviesPlayingOnScreen(
            @RequestParam("playing_status") int num) {
        return movieService.getMoviesPlayingOnScreen(PlayingStatusType.getValueByNum(num));
    }

    @GetMapping("/api/v1/movies/top10")
    public void saveTop10Movie(@RequestParam("target-date") String targetDate) {
        movieService.saveBoxOfficeTop10ByTargetDate(targetDate);
    }

    @PostMapping("/api/v1/admin/movie")
    public void saveMovieByMovieCode(@RequestParam("code") String movieCode) {
        movieService.saveMovieByMovieCode(movieCode);
    }

}
