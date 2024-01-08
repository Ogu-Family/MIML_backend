package com.miml.c2k.domain.schedule.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // TODO: 이 부분 상의
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
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        List<Schedule> retrievedSchedules = scheduleRepository.findAllByMovieIdAndTheaterIdAndDate(
            movie.getId(), theater1.getId(), startTime.toLocalDate());

        assertThat(retrievedSchedules).hasSize(2);
    }
}
