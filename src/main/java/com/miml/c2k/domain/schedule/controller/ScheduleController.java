package com.miml.c2k.domain.schedule.controller;

import com.miml.c2k.domain.schedule.dto.ScheduleViewResponseDto;
import com.miml.c2k.domain.schedule.service.ScheduleService;
import com.miml.c2k.global.dto.ApiResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/schedules")
    public String showSchedulePage() {
        return "schedule/select-schedule";
    }

    @GetMapping("/api/v1/schedules")
    @ResponseBody
    public ApiResponse<List<ScheduleViewResponseDto>> getSchedulesBy(@RequestParam Long movieId,
        @RequestParam Long theaterId,
        @RequestParam LocalDate date) {
        List<ScheduleViewResponseDto> scheduleViewResponseDtos = scheduleService.getSchedulesBy(movieId,
            theaterId, date);

        return ApiResponse.create(HttpStatus.SC_OK, "정상 처리", scheduleViewResponseDtos);
    }
}
