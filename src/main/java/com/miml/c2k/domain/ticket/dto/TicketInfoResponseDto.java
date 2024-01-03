package com.miml.c2k.domain.ticket.dto;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.theater.Theater;
import com.miml.c2k.domain.ticket.Ticket;
import com.miml.c2k.domain.ticket.TicketStatus;
import java.time.LocalDateTime;
import java.util.List;

public record TicketInfoResponseDto(Long id,
                                    String movieTitle,
                                    String poster,
                                    String theaterName,
                                    LocalDateTime startTime,
                                    int screenNum,
                                    List<String> seats,
                                    long paymentFee,
                                    TicketStatus ticketStatus) {

    public static TicketInfoResponseDto create(Ticket ticket, Movie movie, Theater theater, Schedule schedule, Screen screen, Payment payment, List<Seat> seats) {
        return new TicketInfoResponseDto(ticket.getId(),
            movie.getTitle(),
            movie.getPoster(),
            theater.getName(),
            schedule.getStartTime(),
            screen.getNum(),
            seats.stream().map(seat -> seat.getName().name()).toList(),
            payment.getAmount(),
            ticket.getStatus());
    }
}
