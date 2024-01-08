package com.miml.c2k.domain.seat.repository;

import static com.miml.c2k.domain.DataFactoryUtil.createMember;
import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createReservedSeats;
import static com.miml.c2k.domain.DataFactoryUtil.createSchedules;
import static com.miml.c2k.domain.DataFactoryUtil.createScreens;
import static com.miml.c2k.domain.DataFactoryUtil.createTheaters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import com.miml.c2k.domain.ticket.Ticket;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SeatRepositoryTest {

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("상영 일정에 대해 예약된 좌석 목록을 반환한다.")
    void success_findAllReservedSeatsByScheduleId() {
        // given
        Screen screen = screenRepository.saveAll(
                createScreens(theaterRepository.saveAll(createTheaters()))).get(0);
        Schedule schedule = scheduleRepository.saveAll(
                createSchedules(movieRepository.saveAll(createMoviesIsPlaying(1)), screen)).get(0);
        Member member = memberRepository.save(createMember());
        Ticket ticket = ticketRepository.save(Ticket.createWithoutPayment(member, schedule));
        List<Seat> seatsToReserve = createReservedSeats(
                ticket,
                Stream.of(SeatNameType.J11, SeatNameType.J12, SeatNameType.J13).toList());
        seatRepository.saveAll(seatsToReserve);

        // when
        List<Seat> reservedSeats = seatRepository.findAllReservedSeatsByScheduleId(
                schedule.getId());

        // then
        assertThat(reservedSeats, equalTo(seatsToReserve));
    }
}