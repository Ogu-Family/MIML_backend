package com.miml.c2k.domain.ticket.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import com.miml.c2k.domain.theater.Theater;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import com.miml.c2k.domain.ticket.Ticket;
import com.miml.c2k.domain.ticket.dto.TicketInfoResponseDto;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TheaterRepository theaterRepository;
    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("특정 멤버의 모든 티켓 관련 정보들을 가져올 수 있다.")
    void success_get_all_tickets_Info_by_member_id() throws Exception {
        // given
        Long testMemberId = 1L;

        Movie movie = Movie.builder().title("movie").audienceCount(100L).code("1004").build();
        Theater theater = Theater.builder().name("theater").build();
        Screen screen = Screen.builder().num(2).seatCount(20).theater(
            theater).build();
        Schedule schedule = Schedule.builder().startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(1)).movie(movie).screen(screen).build();
        Ticket ticket = Ticket.builder().schedule(schedule).build();
        Seat seat = Seat.builder().name(SeatNameType.J10).screen(screen).build();

        setIdAs1(movie, theater, screen, schedule, ticket);

        ticket.addSeat(seat);

        when(ticketRepository.findAllByMemberId(testMemberId))
            .thenReturn(List.of(ticket));
        when(ticketRepository.findScheduleByTicketId(ticket.getId()))
            .thenReturn(Optional.of(schedule));
        when(scheduleRepository.findMovieByScheduleId(schedule.getId()))
            .thenReturn(Optional.of(movie));
        when(scheduleRepository.findScreenByScheduleId(schedule.getId()))
            .thenReturn(Optional.of(screen));
        when(theaterRepository.findByScreenId(screen.getId()))
            .thenReturn(Optional.of(theater));
        when(ticketRepository.findPaymentByTicketId(ticket.getId()))
            .thenReturn(Optional.of(new Payment()));

        // when
        List<TicketInfoResponseDto> ticketInfoResponseDtos = ticketService.getAllTicketsInfoByMemberId(
            testMemberId);

        // then
        assertThat(ticketInfoResponseDtos).hasSize(1);
        assertThat(ticketInfoResponseDtos.get(0).id()).isEqualTo(ticket.getId());
        assertThat(ticketInfoResponseDtos.get(0).movieTitle()).isEqualTo(movie.getTitle());
        assertThat(ticketInfoResponseDtos.get(0).poster()).isEqualTo(movie.getPoster());
        assertThat(ticketInfoResponseDtos.get(0).theaterName()).isEqualTo(theater.getName());
        assertThat(ticketInfoResponseDtos.get(0).startTime()).isEqualTo(schedule.getStartTime());
        assertThat(ticketInfoResponseDtos.get(0).screenNum()).isEqualTo(screen.getNum());
        assertThat(ticketInfoResponseDtos.get(0).seatNameTypes().get(0)).isEqualTo(SeatNameType.J10);
        assertThat(ticketInfoResponseDtos.get(0).paymentFee()).isEqualTo(0);
        assertThat(ticketInfoResponseDtos.get(0).ticketStatus()).isEqualTo(ticket.getStatus());

        verify(ticketRepository).findAllByMemberId(testMemberId);
        verify(ticketRepository).findScheduleByTicketId(testMemberId);
        verify(ticketRepository).findPaymentByTicketId(1L);
        verify(scheduleRepository).findMovieByScheduleId(1L);
        verify(scheduleRepository).findScreenByScheduleId(1L);
        verify(theaterRepository).findByScreenId(1L);
    }

    @Test
    void success_cancel_ticket_reservation() {
    }

    private void setIdAs1(Object ...obj) throws Exception {
        for (Object o : obj) {
            setId(o, 1L);
        }
    }

    private void setId(Object object, Long id) throws Exception {
        Field ticketIdField = object.getClass().getDeclaredField("id");
        ticketIdField.setAccessible(true);
        ticketIdField.set(object, id);
    }
}
