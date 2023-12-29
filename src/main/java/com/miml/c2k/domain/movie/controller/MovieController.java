package com.miml.c2k.domain.movie.controller;

import com.miml.c2k.domain.movie.dto.MovieResponseDto;
import com.miml.c2k.domain.movie.dto.PlayingStatusType;
import com.miml.c2k.domain.movie.service.MovieService;
import com.miml.c2k.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movies")
    public String getMoviesByPlayingStatus(@RequestParam("playing_status") int num, Model model) {
        List<MovieResponseDto> movies;
        PlayingStatusType playingStatus = PlayingStatusType.getValueByNum(num);
        switch (playingStatus) {
            case IS_PLAYING -> movies = movieService.getMoviesPlayingOnScreen();
            case WILL_PLAYING -> movies = movieService.getMoviesWillPlaying();
            default -> throw new RuntimeException("유효하지 않은 상영 상태 값 입니다.");
        }
        model.addAttribute("movies", movies);
        model.addAttribute("playingStatusTitle", playingStatus.getName());

        return "member/movie-list";
    }

    @GetMapping("/api/v1/movies")
    @ResponseBody
    public ApiResponse<List<MovieResponseDto>> getMoviesPlayingOnScreen() {
        return ApiResponse.create(HttpStatus.SC_OK,
            "영화 목록 정상 반환",
            movieService.getMoviesPlayingOnScreen());
    }
}
