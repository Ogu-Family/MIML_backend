package com.miml.c2k.domain.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.dto.ScheduleViewResponseDto;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
