package com.miml.c2k.domain.schedule.repository;

import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createSchedules;
import static com.miml.c2k.domain.DataFactoryUtil.createScreens;
import static com.miml.c2k.domain.DataFactoryUtil.createTheaters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Test
    @DisplayName("현재 상영하고 있는 영화 목록을 가져온다.")
    void success_findMoviesStartingAfterCurrentTime() {
        // given
        Screen screen = screenRepository.saveAll(
                createScreens(theaterRepository.saveAll(createTheaters()))).get(0);
        scheduleRepository.saveAll(
                createSchedules(movieRepository.saveAll(createMoviesIsPlaying(10)),
                        screen));

        // when
        List<Movie> moviesStartingAfterCurrentTime = scheduleRepository.findMoviesStartingAfterCurrentTime(
                LocalDateTime.now());

        // then: 추출한 영화에 대한 스케줄이 있는지 확인
        assertEquals(10, moviesStartingAfterCurrentTime.size());
        moviesStartingAfterCurrentTime.stream()
                .forEach(movie -> {
                    List<Schedule> schedules = scheduleRepository.findAllByMovieId(
                            movie.getId());
                    assertThat(schedules.size(), greaterThanOrEqualTo(1));
                });
    }

}