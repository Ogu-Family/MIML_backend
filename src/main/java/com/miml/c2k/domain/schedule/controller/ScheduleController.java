package com.miml.c2k.domain.schedule.controller;

import com.miml.c2k.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/schedule")
    public String showSchedulePage() {
        return "schedule/select-schedule";
    }
}
