package com.miml.c2k.domain.ticket.service;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import com.miml.c2k.domain.theater.Theater;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import com.miml.c2k.domain.ticket.Ticket;
import com.miml.c2k.domain.ticket.dto.TicketInfoResponseDto;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public List<TicketInfoResponseDto> getAllTicketsInfoByMemberId(Long memberId) {
        List<TicketInfoResponseDto> ticketInfoResponseDtos = new ArrayList<>();
        // TODO: 결제된 티켓 가져오기
        List<Ticket> tickets = ticketRepository.findAllByMemberId(memberId);

        tickets.forEach(ticket -> {
            ticket.changeStatusCorrectly();
            addTicketInfoResponseDtosByTicketWithoutFetchJoin(ticket, ticketInfoResponseDtos);
        });

        return ticketInfoResponseDtos;
    }

    @Transactional
    public void cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(RuntimeException::new); // TODO: 사용자 정의 예외 생성

        ticket.getSeats().forEach(seat -> seatRepository.deleteById(seat.getId()));
        ticket.getSeats().clear();

        ticket.cancel();
    }

    private void addTicketInfoResponseDtosByTicketWithoutFetchJoin(Ticket ticket,
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

        TicketInfoResponseDto ticketInfoResponseDto = TicketInfoResponseDto.create(ticket,
                relatedMovie,
                relatedTheater, relatedSchedule, relatedScreen, relatedPayment,
                relatedSeats.stream().map(Seat::getName).toList());

        ticketInfoResponseDtos.add(ticketInfoResponseDto);
    }


    @Transactional
    public List<TicketInfoResponseDto> getAllTicketsInfoByMemberIdWithFetchJoin(Long memberId) {
        List<TicketInfoResponseDto> ticketInfoResponseDtos = new ArrayList<>();
        List<Ticket> tickets = ticketRepository.findAllByMemberIdWithFetchJoin(memberId);

        tickets.forEach(ticket -> {
            ticket.changeStatusCorrectly();
            addTicketInfoResponseDtosByTicket(ticket, ticketInfoResponseDtos);
        });

        return ticketInfoResponseDtos;
    }

    private void addTicketInfoResponseDtosByTicket(Ticket ticket,
            List<TicketInfoResponseDto> ticketInfoResponseDtos) {
        Theater theater = ticket.getSchedule().getScreen().getTheater();
        List<Seat> seats = ticket.getSeats();
        Payment payment = ticket.getPayment() == null ? new Payment() : ticket.getPayment();

        TicketInfoResponseDto ticketInfoResponseDto = TicketInfoResponseDto.create(
                ticket, ticket.getSchedule().getMovie(), theater,
                ticket.getSchedule(), ticket.getSchedule().getScreen(),
                payment,
                seats.stream().map(Seat::getName).toList());

        ticketInfoResponseDtos.add(ticketInfoResponseDto);
    }


    @Transactional
    public List<TicketInfoResponseDto> getAllTicketsInfoByMemberIdWithFetchJoinAndAsync(
            Long memberId) {
        Map<Long, TicketInfoResponseDto> ticketInfoResponseDtoMap = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> ticketFutureList = ticketRepository.findAllByMemberIdWithFetchJoin(
                        memberId)
                .stream()
                // 각 티켓에 대해 비동기적으로 작업을 실행
                // 각 티켓마다 실행되는 작업: 티켓 상태 체크 -> 티켓 관련 정보 가져오기
                .map(ticket -> CompletableFuture.runAsync(() -> {
                    ticket.changeStatusCorrectly();
                    addTicketInfoResponseDtoMapByTicket(ticket, ticketInfoResponseDtoMap);
                }))
                .toList();

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(
                ticketFutureList.toArray(new CompletableFuture[ticketFutureList.size()])
        ).join();

        return new ArrayList<>(ticketInfoResponseDtoMap.values());
    }

    private void addTicketInfoResponseDtoMapByTicket(Ticket ticket,
            Map<Long, TicketInfoResponseDto> ticketInfoResponseDtos) {
        Theater theater = ticket.getSchedule().getScreen().getTheater();
        List<Seat> seats = ticket.getSeats();
        Payment payment = ticket.getPayment() == null ? new Payment() : ticket.getPayment();

        TicketInfoResponseDto ticketInfoResponseDto = TicketInfoResponseDto.create(
                ticket, ticket.getSchedule().getMovie(), theater,
                ticket.getSchedule(), ticket.getSchedule().getScreen(),
                payment,
                seats.stream().map(Seat::getName).toList());

        ticketInfoResponseDtos.put(ticket.getId(), ticketInfoResponseDto);
    }
}
