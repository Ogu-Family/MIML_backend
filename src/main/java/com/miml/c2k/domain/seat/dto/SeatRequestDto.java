package com.miml.c2k.domain.seat.dto;

import com.miml.c2k.domain.seat.Seat.SeatNameType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SeatRequestDto {

    private final Long scheduleId;
    private final List<SeatNameType> seatNameTypes;

    @Builder
    public SeatRequestDto(Long scheduleId, List<SeatNameType> seatNameTypes) {
        this.scheduleId = scheduleId;
        this.seatNameTypes = seatNameTypes;
    }
}
