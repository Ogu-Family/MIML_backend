package com.miml.c2k.domain.ticket.service;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.theater.Theater;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import com.miml.c2k.domain.ticket.Ticket;
import com.miml.c2k.domain.ticket.dto.TicketInfoResponseDto;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    private final TheaterRepository theaterRepository;

    public List<TicketInfoResponseDto> getAllTicketsInfoByMemberId(Long memberId) {
        List<TicketInfoResponseDto> ticketInfoResponseDtos = new ArrayList<>();
        // TODO: 결제된 티켓 가져오기
        List<Ticket> tickets = ticketRepository.findAllByMemberId(memberId);

        tickets.parallelStream()
                .forEach(ticket -> addTicketInfoResponseDtosByTicket(ticket,
                        ticketInfoResponseDtos));

        return ticketInfoResponseDtos;
    }

    private void addTicketInfoResponseDtosByTicket(Ticket ticket,
        List<TicketInfoResponseDto> ticketInfoResponseDtos) {
        // TODO: 추후 비동기 처리
        Schedule relatedSchedule = ticketRepository.findScheduleByTicketId(ticket.getId())
            .orElseThrow(() -> new RuntimeException("연결된 상영 일정 없음")); // TODO: 사용자 정의 예외 생성

        Movie relatedMovie = scheduleRepository.findMovieByScheduleId(relatedSchedule.getId())
            .orElseThrow(() -> new RuntimeException("연결된 영화 없음")); // TODO: 사용자 정의 예외 생성

        Screen relatedScreen = scheduleRepository.findScreenByScheduleId(relatedSchedule.getId())
            .orElseThrow(() -> new RuntimeException("연결된 상영관 없음"));// TODO: 사용자 정의 예외 생성

        Theater relatedTheater = theaterRepository.findByScreenId(relatedScreen.getId())
            .orElseThrow(() -> new RuntimeException("연결된 영화관 없음"));// TODO: 사용자 정의 예외 생성

        List<Seat> relatedSeats = ticket.getSeats();

        Payment relatedPayment = ticketRepository.findPaymentByTicketId(ticket.getId())
                .orElseGet(Payment::new);

        TicketInfoResponseDto ticketInfoResponseDto = TicketInfoResponseDto.create(relatedMovie,
                relatedTheater,
                relatedSchedule, relatedScreen, relatedPayment,
                relatedSeats.stream().map(Seat::getName).toList());

        ticketInfoResponseDtos.add(ticketInfoResponseDto);
    }
}
