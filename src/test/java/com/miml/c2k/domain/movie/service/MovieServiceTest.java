package com.miml.c2k.domain.movie.service;

import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createMoviesWillPlaying;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.miml.c2k.domain.movie.dto.MovieResponseDto;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    @DisplayName("현재 상영작 목록을 반환한다.")
    void success_getMoviesPlayingOnScreen() {
        // given
        when(scheduleRepository.findMoviesStartingAfterCurrentTime(
                any(LocalDateTime.class))).thenReturn(createMoviesIsPlaying(10));

        // when
        List<MovieResponseDto> moviesPlayingOnScreen = movieService.getMoviesPlayingOnScreen();

        // then
        assertEquals(10, moviesPlayingOnScreen.size());
    }

    @Test
    @DisplayName("상영 예정작 목록을 반환한다.")
    void success_getMoviesWillPlaying() {
        // given
        when(movieRepository.findMoviesOpeningAfterCurrentDate(any(LocalDate.class))).thenReturn(
                createMoviesWillPlaying(10));

        // when
        List<MovieResponseDto> moviesWillPlaying = movieService.getMoviesWillPlaying();

        // then
        assertEquals(10, moviesWillPlaying.size());
    }

}