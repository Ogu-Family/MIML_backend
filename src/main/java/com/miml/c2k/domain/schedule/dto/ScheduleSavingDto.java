package com.miml.c2k.domain.schedule.dto;

import com.miml.c2k.domain.schedule.Schedule;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ScheduleSavingDto {

    private final Long movieId;
    private final Long screenId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public ScheduleSavingDto(Long movieId, Long screenId, LocalDateTime startTime,
            LocalDateTime endTime) {
        this.movieId = movieId;
        this.screenId = screenId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Schedule toEntity() {
        return new Schedule(startTime, endTime);
    }
}
