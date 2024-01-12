package com.miml.c2k.domain.ticket;

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
import com.miml.c2k.domain.ticket.dto.TicketInfoResponseDto;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import com.miml.c2k.domain.ticket.service.TicketService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TicketInfoFetchJoinAndAsyncTest {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SeatRepository seatRepository;
    private Member member;
    private int ticketCnt = 1000;

    @BeforeEach
    void setUp() {
        Screen screen = screenRepository.saveAll(
                createScreens(theaterRepository.saveAll(createTheaters()))).get(0);
        List<Schedule> schedules = scheduleRepository.saveAll(
                createSchedules(
                        movieRepository.saveAll(createMoviesIsPlaying(ticketCnt)), screen));
        List<SeatNameType> seatNameTypesToReserve = Stream.of(SeatNameType.J11, SeatNameType.J12,
                SeatNameType.J13).toList();
        member = memberRepository.save(createMember());
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
    }

    @AfterEach
    void clear() {
        seatRepository.deleteAll();
        ticketRepository.deleteAll();
        memberRepository.deleteAll();
        scheduleRepository.deleteAll();
        movieRepository.deleteAll();
        screenRepository.deleteAll();
        theaterRepository.deleteAll();
    }

    @Test
    @DisplayName("특정 멤버의 모든 티켓 관련 정보 가져올 수 있다. - Fetch Join X, 비동기 X")
    void success_get_all_tickets_Info_by_member_id() {
        // given

        // when
        long beforeTime = System.currentTimeMillis();

        List<TicketInfoResponseDto> ticketInfoResponseDtos = ticketService.getAllTicketsInfoByMemberId(
                member.getId());

        long afterTime = System.currentTimeMillis();
        long timeTaken = afterTime - beforeTime;
        System.out.println(timeTaken + "ms");

        // then
        assertThat(ticketInfoResponseDtos).hasSize(ticketCnt);
    }

    @Test
    @DisplayName("특정 멤버의 모든 티켓 관련 정보 가져올 수 있다. - Fetch Join O, 비동기 X")
    void success_get_all_tickets_Info_by_member_id_given_fetch_join() {
        // given

        // when
        long beforeTime = System.currentTimeMillis();

        List<TicketInfoResponseDto> ticketInfoResponseDtos = ticketService.getAllTicketsInfoByMemberIdWithFetchJoin(
                member.getId());

        long afterTime = System.currentTimeMillis();
        long timeTaken = afterTime - beforeTime;
        System.out.println(timeTaken + "ms");

        // then
        assertThat(ticketInfoResponseDtos).hasSize(ticketCnt);
    }

    @Test
    @DisplayName("특정 멤버의 모든 티켓 관련 정보 가져올 수 있다. - Fetch Join O, 비동기 O")
    void success_get_all_tickets_Info_by_member_id_given_fetch_join_and_async() {
        // given

        // when
        long beforeTime = System.currentTimeMillis();

        List<TicketInfoResponseDto> ticketInfoResponseDtos = ticketService.getAllTicketsInfoByMemberIdWithFetchJoinAndAsync(
                member.getId());

        long afterTime = System.currentTimeMillis();
        long timeTaken = afterTime - beforeTime;
        System.out.println(timeTaken + "ms");

        // then
        assertThat(ticketInfoResponseDtos).hasSize(ticketCnt);
    }

}
