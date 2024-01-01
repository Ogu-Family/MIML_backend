package com.miml.c2k.domain.seat.controller;

import com.miml.c2k.domain.seat.dto.SeatRequestDto;
import com.miml.c2k.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/api/v1/seats")
    public String getSchedulesBy(
            @RequestParam("schedule_id") Long scheduleId, Model model) {
        model.addAttribute("seats", seatService.getAllSeats(scheduleId));
        return "schedule/select-seat";
    }
}