package com.miml.c2k.domain.admin.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.miml.c2k.domain.admin.dto.MovieAdminResponseDto;
import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.service.MovieService;
import com.miml.c2k.domain.schedule.service.ScheduleService;
import com.miml.c2k.global.auth.SecurityConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;
    @MockBean
    private ScheduleService scheduleService;

    @Test
    @DisplayName("관리자 상영 일정 추가 페이지를 보여줄 수 있다.")
    void success_show_page_adding_schedule() throws Exception {
        List<MovieAdminResponseDto> dtos = new ArrayList<>();
        dtos.add(MovieAdminResponseDto.create(
            Movie.builder().title("영화1").code("1004").genre("스릴러").nation("KR").director("킴남규")
                .openDate(LocalDate.now()).build()));
        dtos.add(MovieAdminResponseDto.create(
            Movie.builder().title("영화2").code("1005").genre("스릴러").nation("KR").director("초이정은")
                .openDate(LocalDate.now()).build()));

        when(movieService.getAllMovies()).thenReturn(dtos);

        mockMvc.perform(get("/admin/schedules"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("movieAdminResponseDtos", dtos));

        verify(movieService).getAllMovies();
    }

    @Test
    @DisplayName("관리자가 상영 일정을 정상적으로 추가할 수 있다.")
    void success_post_request_to_add_schedule() throws Exception {
        mockMvc.perform(
                post("/admin/schedules")
                    .param("movieId", "1")
                    .param("screenId", "1")
                    .param("startTime", LocalDateTime.now().toString())
                    .param("endTime", LocalDateTime.now().toString()))
            .andExpect(status().is3xxRedirection())
            .andDo(print());
    }
}
