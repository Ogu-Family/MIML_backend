package com.miml.c2k.domain.seat.dto;

import com.miml.c2k.domain.seat.Seat.SeatNameType;
import lombok.Getter;

@Getter
public class SeatRequestDto {

    private final Long scheduleId;
    private final SeatNameType seatNameType;

    public SeatRequestDto(Long scheduleId, SeatNameType seatNameType) {
        this.scheduleId = scheduleId;
        this.seatNameType = seatNameType;
    }
}
