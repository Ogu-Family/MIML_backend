package com.miml.c2k.domain.movie.controller;

import com.miml.c2k.domain.movie.service.MovieService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;


    @GetMapping("/api/v1/movie")
    public void saveMovieByMovieCode(@RequestParam("code") String movieCode) {
        movieService.saveMovieByMovieCode(movieCode);
    }

}
