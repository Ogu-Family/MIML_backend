package com.miml.c2k.domain.schedule.controller;

import com.miml.c2k.domain.movie.dto.MovieAdminResponseDto;
import com.miml.c2k.domain.movie.service.MovieService;
import com.miml.c2k.domain.schedule.dto.ScheduleSavingDto;
import com.miml.c2k.domain.schedule.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminScheduleController {

    private final MovieService movieService;
    private final ScheduleService scheduleService;

    @GetMapping("/admin/schedule")
    public String showAddSchedulePage(Model model) {
        List<MovieAdminResponseDto> movieAdminResponseDtos = movieService.getAllMovies();

        model.addAttribute("movieAdminResponseDtos", movieAdminResponseDtos);

        return "admin/admin-schedule";
    }

    @PostMapping("/admin/schedule")
    public String addSchedule(@ModelAttribute ScheduleSavingDto scheduleSavingDto,
        RedirectAttributes redirectAttributes) {
        scheduleService.saveSchedule(scheduleSavingDto);

        redirectAttributes.addFlashAttribute("msg", "성공적으로 등록 되었습니다.");

        return "redirect:/admin/schedule";
    }
}
