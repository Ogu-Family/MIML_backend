package com.miml.c2k.domain.schedule.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.dto.ScheduleSavingDto;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ScreenRepository screenRepository;
    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    @DisplayName("saveSchedule 메서드가 정상적인 ScheduleSavingDto 객체에 한해 잘 실행되는지 확인한다.")
    void success_save_schedule_given_normal_input() throws Exception{
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now();

        ScheduleSavingDto scheduleSavingDto = new ScheduleSavingDto(1L, 1L, startTime, endTime);

        Movie retrievedMovie = Movie.builder().title("temp").build();
        Field movieId = retrievedMovie.getClass().getDeclaredField("id");
        movieId.setAccessible(true);
        movieId.set(retrievedMovie, 1L);

        Screen retrievedScreen = Screen.builder().num(1).build();
        Field screenId = retrievedScreen.getClass().getDeclaredField("id");
        screenId.setAccessible(true);
        screenId.set(retrievedScreen, 1L);

        when(movieRepository.findById(1L))
            .thenReturn(Optional.of(retrievedMovie));
        when(screenRepository.findById(1L))
            .thenReturn(Optional.of(retrievedScreen));

        scheduleService.saveSchedule(scheduleSavingDto);

        verify(movieRepository).findById(1L);
        verify(screenRepository).findById(1L);
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    @DisplayName("시작 시간이 끝 시간보다 뒤인 모순된 입력에 대해 saveSchedule 메서드가 예외를 발생시킨다.")
    void fail_save_schedule_given_abnormal_input() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 26, 5, 30);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 1, 5, 30);

        ScheduleSavingDto scheduleSavingDto = new ScheduleSavingDto(1L, 1L, startTime, endTime);

        assertThrows(RuntimeException.class,
            () -> scheduleService.saveSchedule(scheduleSavingDto));
    }
}
