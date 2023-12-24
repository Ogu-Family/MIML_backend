package com.miml.c2k.domain.schedule.service;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.dto.ScheduleSavingDto;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

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

    private void validateNewSchedule(ScheduleSavingDto scheduleSavingDto) {
        int count = scheduleRepository.countAllByScreenBetweenTimeline(scheduleSavingDto.getScreenId(),
            scheduleSavingDto.getStartTime(),
            scheduleSavingDto.getEndTime());

        if (count > 0 || scheduleSavingDto.getStartTime().isAfter(scheduleSavingDto.getEndTime())) {
            throw new RuntimeException(); // TODO: 사용자 정의 예외 생성
        }
    }
}
