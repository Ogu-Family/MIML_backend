package com.miml.c2k.domain.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.dto.ScheduleSavingDto;
import com.miml.c2k.domain.schedule.dto.ScheduleViewResponseDto;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    void fail_save_schedule_given_abnormal_timeline_input() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 26, 5, 30);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 1, 5, 30);

        ScheduleSavingDto scheduleSavingDto = new ScheduleSavingDto(1L, 1L, startTime, endTime);

        assertThrows(RuntimeException.class,
            () -> scheduleService.saveSchedule(scheduleSavingDto));
    }

    @Test
    @DisplayName("상영하려는 시간 대에 해당 상영관에서 이미 상영 중인 영화가 있다면 예외를 발생시킨다.")
    void fail_save_schedule_given_any_schedules_already_exist_in_timeline() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);

        ScheduleSavingDto scheduleSavingDto = new ScheduleSavingDto(1L, 2L, startTime, endTime);

        when(scheduleRepository.countAllByScreenIdBetweenTimeline(2L, startTime, endTime))
            .thenReturn(2);

        assertThrows(RuntimeException.class, () -> scheduleService.saveSchedule(scheduleSavingDto));

        verify(scheduleRepository).countAllByScreenIdBetweenTimeline(2L, startTime, endTime);
    }

    @Test
    @DisplayName("받아온 영화 id, 영화관 id, 선택된 날짜에 해당하는 상영 일정들을 가져온다.")
    void success_get_schedules_by_infos() throws Exception {
        // given
        LocalDate nowLocalDate = LocalDate.now();

        List<Schedule> schedules = new ArrayList<>();

        Screen screen = Screen.builder().seatCount(25).build();

        Schedule testSchedule1 = Schedule.builder()
            .startTime(nowLocalDate.atTime(12, 0))
            .endTime(nowLocalDate.atTime(14, 0))
            .screen(screen)
            .build();
        Schedule testSchedule2 = Schedule.builder()
            .startTime(nowLocalDate.atTime(12, 0))
            .endTime(nowLocalDate.atTime(14, 0))
            .screen(screen)
            .build();

        Field scheduleIdField = Schedule.class.getDeclaredField("id");

        scheduleIdField.setAccessible(true);
        scheduleIdField.set(testSchedule1, 1L);
        scheduleIdField.set(testSchedule2, 2L);

        schedules.add(testSchedule1);
        schedules.add(testSchedule2);

        when(scheduleRepository.findAllByMovieIdAndTheaterIdAndDate(1L, 1L, nowLocalDate))
            .thenReturn(schedules);
        when(seatRepository.countReservedSeatsByScheduleId(any(Long.class)))
            .thenReturn(20);

        // when
        List<ScheduleViewResponseDto> scheduleViewResponseDtos = scheduleService.getSchedulesBy(1L,
            1L, nowLocalDate);

        // then
        assertThat(scheduleViewResponseDtos).hasSize(2);
        assertThat(scheduleViewResponseDtos.get(0).getAvailableSeatsCount()).isEqualTo(5);

        verify(scheduleRepository).findAllByMovieIdAndTheaterIdAndDate(1L, 1L, nowLocalDate);
        verify(seatRepository, times(2)).countReservedSeatsByScheduleId(any());
    }
}
