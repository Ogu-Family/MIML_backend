package com.miml.c2k.domain.schedule.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.miml.c2k.domain.schedule.dto.ScheduleViewResponseDto;
import com.miml.c2k.domain.schedule.service.ScheduleService;
import com.miml.c2k.global.auth.SecurityConfig;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ScheduleController.class)
@AutoConfigureRestDocs
@Import(SecurityConfig.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScheduleService scheduleService;

    @Test
    @DisplayName("영화 상영 선택 페이지를 볼 수 있다.")
    void success_show_schedule_page() throws Exception {
        mockMvc.perform(get("/schedules"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주어진 정보들을 이용해 상영 일정을 가져올 수 있다.")
    void success_get_schedules_by_infos() throws Exception {
        List<ScheduleViewResponseDto> scheduleViewResponseDtos = new ArrayList<>();
        LocalDate nowLocalDate = LocalDate.now();

        ScheduleViewResponseDto data1 = new ScheduleViewResponseDto(1L, 2, 100, 80,
            nowLocalDate.atTime(1, 0), nowLocalDate.atTime(3, 0), 1000);
        ScheduleViewResponseDto data2 = new ScheduleViewResponseDto(2L, 2, 100, 90,
            nowLocalDate.atTime(5, 0), nowLocalDate.atTime(7, 0), 1000);

        scheduleViewResponseDtos.add(data1);
        scheduleViewResponseDtos.add(data2);

        when(scheduleService.getSchedulesBy(1L, 2L, nowLocalDate))
            .thenReturn(scheduleViewResponseDtos);

        mockMvc.perform(get("/api/v1/schedules")
                .param("movieId", "1")
                .param("theaterId", "2")
                .param("date", nowLocalDate.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(1L))
            .andExpect(jsonPath("$.data[0].screenNum").value(2))
            .andExpect(jsonPath("$.data[1].id").value(2L))
            .andDo(print())
            .andDo(document("getSchedulesBy",
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(NUMBER).description("처리 코드"),
                    fieldWithPath("message").type(STRING).description("처리 메시지"),
                    fieldWithPath("data[]").type(ARRAY).description("가져온 상영 일정 리스트"),
                    fieldWithPath("data[].id").type(NUMBER).description("상영 일정 id"),
                    fieldWithPath("data[].screenNum").type(NUMBER).description("상영관 번호"),
                    fieldWithPath("data[].totalSeatsCount").type(NUMBER).description("상영관 전체 좌석 수"),
                    fieldWithPath("data[].availableSeatsCount").type(NUMBER).description("상영관 가용 좌석 수"),
                    fieldWithPath("data[].startTime").type(STRING).description("상영 시작 시간"),
                    fieldWithPath("data[].endTime").type(STRING).description("상영 끝나는 시간"),
                    fieldWithPath("data[].fee").type(NUMBER).description("가격")
                )));
    }
}
