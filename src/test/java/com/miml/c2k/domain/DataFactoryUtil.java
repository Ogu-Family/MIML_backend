package com.miml.c2k.domain;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.OAuthProvider;
import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.theater.Theater;
import com.miml.c2k.domain.ticket.Ticket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static Member createMember() {
        return Member.builder()
                .email("test@case.com")
                .nickname("testNickName")
                .oAuthProvider(OAuthProvider.KAKAO)
                .build();
    }

    public static List<Theater> createTheaters() {
        return Arrays.asList(
                Theater.builder().name("서울 중랑점").build(),
                Theater.builder().name("경기도 부천점").build(),
                Theater.builder().name("부산 서면점").build()
        );
    }

    public static List<Screen> createScreens(List<Theater> theaters) {
        List<Screen> screens = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            screens.add(Screen.builder().num(i).seatCount(10).theater(theaters.get(0)).build());
        }
        for (int i = 1; i <= 10; i++) {
            screens.add(Screen.builder().num(i).seatCount(10).theater(theaters.get(1)).build());
        }
        for (int i = 1; i <= 10; i++) {
            screens.add(Screen.builder().num(i).seatCount(10).theater(theaters.get(2)).build());
        }

        return screens;
    }

    public static List<Seat> createReservedSeats(Ticket ticket, List<SeatNameType> seatNameTypes) {
        return seatNameTypes.stream().map(
                        seatNameType -> createReservedSeat(ticket, seatNameType))
                .toList();
    }

    private static Seat createReservedSeat(Ticket ticket, SeatNameType seatNameType) {
        return Seat.builder()
                .name(seatNameType)
                .screen(ticket.getSchedule().getScreen())
                .ticket(ticket)
                .build();
    }
}