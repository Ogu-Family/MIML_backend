package com.miml.c2k.domain.schedule.repository;

import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createSchedules;
import static com.miml.c2k.domain.DataFactoryUtil.createScreens;
import static com.miml.c2k.domain.DataFactoryUtil.createTheaters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.theater.Theater;
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
    @DisplayName("특정 날짜, 영화, 영화관에 해당하는 상영 일정을 전부 가져온다.")
    void success_find_all_by_movie_id_and_theater_id_and_date() {
        Movie movie = Movie.builder().title("title").code("1204").audienceCount(12392L).build();
        movieRepository.save(movie);

        Theater theater1 = Theater.builder().name("theater1").build();
        Theater theater2 = Theater.builder().name("theater2").build();
        theaterRepository.saveAll(List.of(theater1, theater2));

        Screen screen1 = Screen.builder().num(1).seatCount(20).theater(theater1).build();
        Screen screen2 = Screen.builder().num(2).seatCount(30).theater(theater1).build();
        Screen screen3 = Screen.builder().num(2).seatCount(30).theater(theater2).build();
        screenRepository.saveAll(List.of(screen1, screen2, screen3));

        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 5, 30);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 1, 7, 30);

        Schedule schedule1 = Schedule.builder().startTime(startTime).endTime(endTime)
            .movie(movie).screen(screen1)
            .build();
        Schedule schedule2 = Schedule.builder().startTime(startTime).endTime(endTime)
            .movie(movie).screen(screen2)
            .build();
        Schedule schedule3 = Schedule.builder().startTime(startTime).endTime(endTime)
            .movie(movie).screen(screen3)
            .build();
        Schedule schedule4 = Schedule.builder().startTime(startTime.plusDays(1)).endTime(endTime.plusDays(1))
            .movie(movie).screen(screen1)
            .build();
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3, schedule4));

        List<Schedule> retrievedSchedules = scheduleRepository.findAllByMovieIdAndTheaterIdAndDate(
            movie.getId(), theater1.getId(), startTime.toLocalDate());

        assertThat(retrievedSchedules).hasSize(2);
    }

    @Test
    @DisplayName("countAllByScreenIdBetweenTimeline 메서드 쿼리로 startTime, endTime 사이 상영 일정 개수를 정확히 셀 수 있다.")
    void success_count_all_by_screen_id_between_timeline() {
        // given
        Screen screen = Screen.builder().seatCount(20).num(1).build();

        Schedule schedule1 = Schedule.builder().screen(screen)
            .startTime(LocalDateTime.of(2024, 1, 8, 21, 0))
            .endTime(LocalDateTime.of(2024, 1, 8, 22, 30)).build();
        Schedule schedule2 = Schedule.builder().screen(screen)
            .startTime(LocalDateTime.of(2024, 1, 8, 22, 50))
            .endTime(LocalDateTime.of(2024, 1, 8, 23, 55)).build();

        screenRepository.save(screen);
        scheduleRepository.saveAll(List.of(schedule1, schedule2));

        // when
        int scheduleCountBetween22To23 = scheduleRepository.countAllByScreenIdBetweenTimeline(schedule1.getId(),
            LocalDateTime.of(2024, 1, 8, 22, 0), LocalDateTime.of(2024, 1, 8, 23, 0));
        int scheduleCountBetween23To24 = scheduleRepository.countAllByScreenIdBetweenTimeline(schedule1.getId(),
            LocalDateTime.of(2024, 1, 8, 23, 0), LocalDateTime.of(2024, 1, 9, 0, 0));

        assertThat(scheduleCountBetween22To23).isEqualTo(2);
        assertThat(scheduleCountBetween23To24).isEqualTo(1);
    }

    @Test
    @DisplayName("현재 상영하고 있는 영화 목록을 가져온다.")
    void success_find_movies_starting_after_current_time() {
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
