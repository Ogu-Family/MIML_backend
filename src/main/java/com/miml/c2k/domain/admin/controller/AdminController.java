package com.miml.c2k.domain.admin.controller;

import com.miml.c2k.domain.admin.dto.MovieAdminResponseDto;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;
    private final ScheduleService scheduleService;

    @GetMapping("/admin/schedule")
    public String showAddSchedulePage(Model model) {
        List<MovieAdminResponseDto> movieAdminResponseDtos = movieService.getAllMovies();

        model.addAttribute("movieAdminResponseDtos", movieAdminResponseDtos);

        return "admin/admin-schedule";
    }

    @PostMapping("/admin/schedule")
    public String addSchedule(@ModelAttribute ScheduleSavingDto scheduleSavingDto) {
        scheduleService.saveSchedule(scheduleSavingDto);

        return "redirect:/admin/schedule";
    }

    @GetMapping("/admin/movies/insert")
    public String showInsertMoviePage() {
        return "admin/insert-movie";
    }

    @PostMapping("/admin/movie")
    public String saveMovieByMovieCode(@RequestParam("code") String movieCode) {
        movieService.saveMovieByMovieCode(movieCode);

        return "admin/insert-movie";
    }

    @PostMapping("/admin/movies/top10")
    public String saveTop10Movie(@RequestParam("target_date") String targetDate) {
        movieService.saveBoxOfficeTop10ByTargetDate(targetDate);

        return "admin/insert-movie";
    }
}
