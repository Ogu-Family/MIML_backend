package com.miml.c2k.domain.seat.controller;

import com.miml.c2k.domain.seat.dto.ReservedSeatResponseDto;
import com.miml.c2k.domain.seat.dto.SeatRequestDto;
import com.miml.c2k.domain.seat.service.SeatService;
import com.miml.c2k.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seats")
    public String getSchedulesBy(
            @RequestParam("schedule_id") Long scheduleId, Model model) {
        model.addAttribute("seats", seatService.getAllSeats(scheduleId));
        return "schedule/select-seat";
    }

    @PostMapping("/api/v1/seats")
    @ResponseBody
    public ApiResponse<ReservedSeatResponseDto> getSchedulesBy(
            @RequestBody SeatRequestDto seatRequestDto) {
        return ApiResponse.create(HttpStatus.SC_OK, "정상 처리",
                seatService.reserveSeat(seatRequestDto));
    }
}