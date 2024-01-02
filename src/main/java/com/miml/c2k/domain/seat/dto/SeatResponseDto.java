package com.miml.c2k.domain.seat.dto;

import com.miml.c2k.domain.seat.Seat.SeatNameType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SeatResponseDto {

    private final Long scheduleId;
    private final SeatNameType seatNameType;
    private final boolean isReserved;

    @Builder
    public SeatResponseDto(Long scheduleId, SeatNameType seatNameType, boolean isReserved) {
        this.scheduleId = scheduleId;
        this.seatNameType = seatNameType;
        this.isReserved = isReserved;
    }
}
