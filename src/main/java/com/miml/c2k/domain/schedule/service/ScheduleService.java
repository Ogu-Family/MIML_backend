package com.miml.c2k.domain.schedule.service;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.dto.ScheduleSavingDto;
import com.miml.c2k.domain.schedule.dto.ScheduleViewResponseDto;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;

    public void saveSchedule(ScheduleSavingDto scheduleSavingDto) {
        validateNewSchedule(scheduleSavingDto);

        Schedule schedule = scheduleSavingDto.toEntity();
        Movie movie = movieRepository.findById(scheduleSavingDto.getMovieId()).orElseThrow(
            EntityNotFoundException::new); // TODO: 사용자 정의 예외 생성
        Screen screen = screenRepository.findById(scheduleSavingDto.getScreenId())
            .orElseThrow(EntityNotFoundException::new); // TODO: 사용자 정의 예외 생성

        schedule.updateMovie(movie);
        schedule.updateScreen(screen);

        scheduleRepository.save(schedule);
    }

    public List<ScheduleViewResponseDto> getSchedulesBy(Long movieId, Long theaterId,
        LocalDate date) {
        List<Schedule> allSchedules = scheduleRepository.findAllByMovieIdAndTheaterIdAndDate(
            movieId, theaterId, date);

        return getScheduleViewResponseDtos(allSchedules);
    }

    private List<ScheduleViewResponseDto> getScheduleViewResponseDtos(List<Schedule> allSchedules) {
        return allSchedules.stream().map(schedule -> {
                int reservedSeats = seatRepository.countReservedSeatsByScheduleId(schedule.getId());

                return ScheduleViewResponseDto.create(schedule,
                    schedule.getScreen().getSeatCount() - reservedSeats);
            })
            .filter(dto -> dto.getAvailableSeatsCount() > 0)
            .toList();
    }

    private void validateNewSchedule(ScheduleSavingDto scheduleSavingDto) {
        int count = scheduleRepository.countAllByScreenIdBetweenTimeline(
            scheduleSavingDto.getScreenId(),
            scheduleSavingDto.getStartTime(),
            scheduleSavingDto.getEndTime());

        if (count > 0 || scheduleSavingDto.getStartTime().isAfter(scheduleSavingDto.getEndTime())) {
            throw new RuntimeException(); // TODO: 사용자 정의 예외 생성
        }
    }
}
