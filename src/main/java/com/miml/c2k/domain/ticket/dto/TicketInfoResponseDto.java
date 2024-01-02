package com.miml.c2k.domain.ticket.dto;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.theater.Theater;
import java.time.LocalDateTime;

public record TicketInfoResponseDto(String movieTitle,
                                    String poster,
                                    String theaterName,
                                    LocalDateTime startTime,
                                    int screenNum,
                                    long paymentFee) {

    public TicketInfoResponseDto create(Movie movie, Theater theater, Schedule schedule, Screen screen, Payment payment) {
        return new TicketInfoResponseDto(movie.getTitle(),
            movie.getPoster(),
            theater.getName(),
            schedule.getStartTime(),
            screen.getNum(),
            payment.getAmount());
    }
}
