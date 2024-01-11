package com.miml.c2k.domain.seat.service;

import static com.miml.c2k.domain.DataFactoryUtil.createMember;
import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createReservedSeats;
import static com.miml.c2k.domain.DataFactoryUtil.createSchedules;
import static com.miml.c2k.domain.DataFactoryUtil.createScreens;
import static com.miml.c2k.domain.DataFactoryUtil.createTheaters;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.dto.ReservedSeatResponseDto;
import com.miml.c2k.domain.seat.dto.SeatRequestDto;
import com.miml.c2k.domain.seat.dto.SeatResponseDto;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import com.miml.c2k.domain.ticket.Ticket;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    private SeatService seatService;

    @Test
    @DisplayName("상영 일정에 해당하는 상영관의 좌석 정보 목록을 반환한다.")
    void success_getAllSeats() {
        // given
        Screen screen = createScreens(createTheaters()).get(0);
        Schedule schedule = createSchedules(
                createMoviesIsPlaying(1),
                screen).get(0);
        Ticket ticket = Ticket.createWithoutPayment(createMember(), schedule);
        List<Seat> reservedSeats = createReservedSeats(
                ticket,
                Stream.of(SeatNameType.J11, SeatNameType.J12, SeatNameType.J13).toList());
        when(seatRepository.findAllReservedSeatsByScheduleId(schedule.getId())).thenReturn(
                reservedSeats);

        // when
        List<SeatResponseDto> allSeatResponseDtos = seatService.getAllSeats(schedule.getId());

        // then: 총 좌석 수 및 예약된 좌석 수를 확인
        assertThat(allSeatResponseDtos, hasSize(SeatNameType.values().length));
        assertThat(allSeatResponseDtos.stream().filter(SeatResponseDto::isReserved).toList(),
                hasSize(reservedSeats.size()));
    }

    @Test
    @DisplayName("좌석을 예매한다.")
    void success_reserveSeat() {
        // given
        Screen screen = createScreens(createTheaters()).get(0);
        Schedule schedule = createSchedules(
                createMoviesIsPlaying(1),
                screen).get(0);
        Ticket ticket = Ticket.createWithoutPayment(createMember(), schedule);
        List<Seat> reservedSeats = createReservedSeats(
                ticket,
                Stream.of(SeatNameType.J11, SeatNameType.J12, SeatNameType.J13).toList());
        Member member = createMember();
        SeatRequestDto seatRequestDto = SeatRequestDto.builder()
                .scheduleId(schedule.getId())
                .seatNameTypes(
                        Stream.of(SeatNameType.J14, SeatNameType.J15, SeatNameType.J16).toList())
                .build();

        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(seatRepository.findAllReservedSeatsByScheduleId(schedule.getId())).thenReturn(
                reservedSeats);

        // when
        ReservedSeatResponseDto reservedSeatResponseDto
                = seatService.reserveSeat(seatRequestDto, member.getId());

        // then: 티켓 아이디 및 예약된 좌석 수 확인
        assertThat(reservedSeatResponseDto.getTicketId(), is(ticket.getId()));
        assertThat(reservedSeatResponseDto.getSeatInfos(),
                hasSize(seatRequestDto.getSeatNameTypes().size()));

    }

    @Test
    @DisplayName("이미 예약된 좌석 예매 시 예외를 던진다.")
    void fail_reserveSeat_given_alreadyReservedSeat() {
        // given
        Screen screen = createScreens(createTheaters()).get(0);
        Schedule schedule = createSchedules(
                createMoviesIsPlaying(1),
                screen).get(0);
        SeatNameType alreadyReservedSeat = SeatNameType.J11;
        Ticket ticket = Ticket.createWithoutPayment(createMember(), schedule);
        List<Seat> reservedSeats = createReservedSeats(
                ticket,
                Stream.of(SeatNameType.J11, SeatNameType.J12, SeatNameType.J13).toList());
        Member member = createMember();
        SeatRequestDto seatRequestDto = SeatRequestDto.builder()
                .scheduleId(schedule.getId())
                .seatNameTypes(
                        Stream.of(alreadyReservedSeat, SeatNameType.J15, SeatNameType.J16).toList())
                .build();

        when(seatRepository.findAllReservedSeatsByScheduleId(schedule.getId())).thenReturn(
                reservedSeats);

        // when
        RuntimeException exception
                = assertThrows(RuntimeException.class,
                () -> seatService.reserveSeat(seatRequestDto, member.getId()));

        // then
        assertThat(exception.getMessage(), equalTo(alreadyReservedSeat + "는 이미 예약된 좌석입니다."));
    }
}