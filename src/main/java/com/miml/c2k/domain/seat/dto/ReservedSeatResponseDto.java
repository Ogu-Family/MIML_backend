package com.miml.c2k.domain.seat.dto;

import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.ticket.Ticket;
import lombok.Getter;

@Getter
public class ReservedSeatResponseDto {

    private final Long seatId;
    private final Long ticketId;
    private final SeatNameType seatNameType;

    public ReservedSeatResponseDto(Long seatId, Long ticketId, SeatNameType seatNameType) {
        this.seatId = seatId;
        this.ticketId = ticketId;
        this.seatNameType = seatNameType;
    }

    public static ReservedSeatResponseDto create(Seat seat, Ticket ticket) {
        return new ReservedSeatResponseDto(seat.getId(), ticket.getId(), seat.getName());
    }
}
