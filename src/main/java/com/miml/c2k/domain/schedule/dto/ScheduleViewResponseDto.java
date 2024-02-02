package com.miml.c2k.domain.schedule.dto;

import com.miml.c2k.domain.schedule.Schedule;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ScheduleViewResponseDto {

    private final Long id;
    private final Integer screenNum;
    private final Integer totalSeatsCount;
    private final Integer availableSeatsCount;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Integer fee;

    public ScheduleViewResponseDto(Long id, Integer screenNum, Integer totalSeatsCount,
        Integer availableSeatsCount, LocalDateTime startTime, LocalDateTime endTime, Integer fee) {
        this.id = id;
        this.screenNum = screenNum;
        this.totalSeatsCount = totalSeatsCount;
        this.availableSeatsCount = availableSeatsCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }

    public static ScheduleViewResponseDto create(Schedule schedule, Integer availableSeatCnt) {
        validateParameters(schedule, availableSeatCnt);

        return new ScheduleViewResponseDto(schedule.getId(),
            schedule.getScreen().getNum(),
            schedule.getScreen().getSeatCount(),
            availableSeatCnt,
            schedule.getStartTime(),
            schedule.getEndTime(),
            schedule.getFee());
    }

    private static void validateParameters(Schedule schedule, Integer availableSeatCnt) {
        if (schedule.getId() == null || availableSeatCnt < 0) {
            throw new RuntimeException(); // TODO: 사용자 예외 설정
        }
    }
}
