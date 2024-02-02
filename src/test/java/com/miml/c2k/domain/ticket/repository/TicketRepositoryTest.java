package com.miml.c2k.domain.ticket.repository;

import static com.miml.c2k.domain.DataFactoryUtil.createMember;
import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createSchedules;
import static com.miml.c2k.domain.DataFactoryUtil.createScreens;
import static com.miml.c2k.domain.DataFactoryUtil.createTheaters;
import static org.assertj.core.api.Assertions.assertThat;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import com.miml.c2k.domain.ticket.Ticket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("member id로 특정 회원의 티켓들을 가져온다.")
    void success_find_all_tickets_by_member_id() {
        // given
        Member member = Member.builder().nickname("nick").email("cho@gmail.com").build();
        Ticket ticket1 = Ticket.builder().member(member).build();
        Ticket ticket2 = Ticket.builder().member(member).build();
        Seat seat1 = Seat.builder().name(SeatNameType.J10).build();
        Seat seat2 = Seat.builder().name(SeatNameType.J10).build();

        ticket1.addSeat(seat1);
        ticket2.addSeat(seat2);

        memberRepository.save(member);
        ticketRepository.saveAll(List.of(ticket1, ticket2));
        seatRepository.saveAll(List.of(seat1, seat2));

        // when
        List<Ticket> tickets = ticketRepository.findAllByMemberId(member.getId());

        // then
        assertThat(tickets).hasSize(2);
    }

    @Test
    @DisplayName("member id로 특정 회원의 티켓들을 fetch join 하여 가져온다.")
    void success_find_all_tickets_with_fetch_join_by_member_id_() {
        // given
        int ticketCnt = 3;
        Screen screen = screenRepository.saveAll(
                createScreens(theaterRepository.saveAll(createTheaters()))).get(0);
        List<Schedule> schedules = scheduleRepository.saveAll(
                createSchedules(
                        movieRepository.saveAll(createMoviesIsPlaying(ticketCnt)), screen));
        List<SeatNameType> seatNameTypesToReserve = Stream.of(SeatNameType.J11, SeatNameType.J12,
                SeatNameType.J13).toList();
        Member member = memberRepository.save(createMember());
        schedules.forEach(schedule -> {
            Ticket ticket = ticketRepository.save(
                    Ticket.builder().member(member).schedule(schedule).build());
            List<Seat> seats = seatNameTypesToReserve.stream()
                    .map(seatNameType -> Seat.builder()
                            .name(seatNameType)
                            .screen(schedule.getScreen())
                            .ticket(ticket)
                            .build())
                    .collect(Collectors.toList());
            seatRepository.saveAll(seats);
            seats.forEach(ticket::addSeat);
        });

        // when
        List<Ticket> tickets = ticketRepository.findAllByMemberIdWithFetchJoin(member.getId());

        // then
        assertThat(tickets).hasSize(ticketCnt);
    }

    @Test
    @DisplayName("특정 티켓과 연결된 상영 일정을 가져온다.")
    void success_find_a_schedule_by_ticket_id() {
        // given
        Schedule schedule = Schedule.builder().startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now()).build();
        Ticket ticket = Ticket.builder().schedule(schedule).build();

        scheduleRepository.save(schedule);
        ticketRepository.save(ticket);

        // when
        Optional<Schedule> retrievedSchedule = ticketRepository.findScheduleByTicketId(
                ticket.getId());

        // then
        assertThat(retrievedSchedule.isPresent()).isTrue();
        assertThat(retrievedSchedule.get().getId()).isEqualTo(schedule.getId());
    }

    @Test
    @Disabled
        // TODO: 결제 구현 후 작성
    void success_find_payment_by_ticket_id() {
    }
}
