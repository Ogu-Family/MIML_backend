package com.miml.c2k.domain;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.screen.Screen;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DataFactoryUtil {

    public static List<Movie> createMoviesIsPlaying(int cnt) {
        List<Movie> moviesIsPlaying = new ArrayList<>();
        LocalDate beforeToday = LocalDate.now().minusDays(1);
        for (int i = 0; i < cnt; i++) {
            moviesIsPlaying.add(
                    Movie.builder().openDate(beforeToday).genre("testGenre").title("testTitle")
                            .nation("testNation").director("testDirector")
                            .code("isplayingMovieTestCode" + i).build());
        }

        return moviesIsPlaying;
    }

    public static List<Movie> createMoviesWillPlaying(int cnt) {
        List<Movie> moviesWillPlaying = new ArrayList<>();
        LocalDate afterToday = LocalDate.now().plusDays(1);
        for (int i = 0; i < cnt; i++) {
            moviesWillPlaying.add(
                    Movie.builder().openDate(afterToday).genre("testGenre").title("testTitle")
                            .nation("testNation").director("testDirector")
                            .code("willPlayingMovieTestCode" + i).build());
        }

        return moviesWillPlaying;
    }

    public static List<Schedule> createSchedules(List<Movie> moviesIsPlaying, Screen screen) {
        List<Schedule> schedules = new ArrayList<>();

        LocalDateTime currentTime = LocalDateTime.now();
        for (Movie movie : moviesIsPlaying) {
            Schedule schedule = Schedule.builder().startTime(currentTime.plusHours(1))
                    .endTime(currentTime.plusHours(3)).build();
            schedule.updateMovie(movie);
            schedule.updateScreen(screen);

            schedules.add(schedule);
        }

        return schedules;
    }

}
