package com.miml.c2k.domain.seat.dto;

import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservedSeatResponseDto {

    private final Long ticketId;
    private final List<SeatInfo> seatInfos;

    public static ReservedSeatResponseDto create(Long ticketId, List<Seat> seats) {
        return new ReservedSeatResponseDto(ticketId,
                seats.stream().map(seat -> new SeatInfo(seat.getId(), seat.getName())).toList());
    }

    @RequiredArgsConstructor
    @Getter
    private static class SeatInfo {

        private final Long seatId;
        private final SeatNameType seatNameType;
    }
}
