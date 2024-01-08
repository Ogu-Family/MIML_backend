package com.miml.c2k.domain.seat.controller;

import static com.miml.c2k.domain.DataFactoryUtil.createMember;
import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createSchedules;
import static com.miml.c2k.domain.DataFactoryUtil.createScreens;
import static com.miml.c2k.domain.DataFactoryUtil.createTheaters;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.dto.SeatRequestDto;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import com.miml.c2k.global.auth.jwt.AuthTokensGenerator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    @BeforeEach
    void setUp() {
        Screen screen = screenRepository.saveAll(
                createScreens(theaterRepository.saveAll(createTheaters()))).get(0);
        scheduleRepository.saveAll(
                createSchedules(movieRepository.saveAll(createMoviesIsPlaying(1)), screen)).get(0);
    }

    @AfterEach
    void clear() {
        seatRepository.deleteAll();
        ticketRepository.deleteAll();
        memberRepository.deleteAll();
        scheduleRepository.deleteAll();
        movieRepository.deleteAll();
        screenRepository.deleteAll();
        theaterRepository.deleteAll();
    }

    @Test
    @DisplayName("상영 일정에 대한 좌석 목록 뷰를 요청한다.")
    void success_getAllSeatsView() throws Exception {
        // given
        Schedule schedule = scheduleRepository.findById(1L).get();
        Long seatCountToReserve = 3L;

        // when
        ResultActions resultActions = mockMvc.perform(get("/seats")
                .param("schedule_id", schedule.getId().toString())
                .param("seat_count", String.valueOf(seatCountToReserve)));

        // then
        resultActions.andExpect(view().name("schedule/select-seat"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("seats", hasSize(SeatNameType.values().length)))
                .andExpect(model().attribute("seat_count", equalTo(seatCountToReserve)));
    }

    @Test
    @DisplayName("상영 일정에 대해 좌석을 예매한다.")
    void success_reserveSeats() throws Exception {
        // given
        Schedule schedule = scheduleRepository.findAll().get(0);
        List<SeatNameType> seatNameTypesToReserve = Stream.of(SeatNameType.J11, SeatNameType.J12,
                SeatNameType.J13).toList();
        SeatRequestDto seatRequestDto = SeatRequestDto.builder()
                .scheduleId(schedule.getId())
                .seatNameTypes(seatNameTypesToReserve)
                .build();
        Member member = memberRepository.save(createMember());
        String accessToken = authTokensGenerator.generate(member.getId()).getAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/seats")
                .header("accessToken", accessToken)
                .content(objectMapper.writeValueAsString(seatRequestDto))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("좌석 예매 성공"))
                .andExpect(jsonPath("$.data.seatInfos.size()").value(seatNameTypesToReserve.size()))
                .andDo(document("reserveSeats",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("message").description("성공 메시지"),
                                fieldWithPath("data.ticketId").description("티켓 아이디"),
                                fieldWithPath("data.seatInfos.[].seatId").description("예매한 좌석 아이디"),
                                fieldWithPath("data.seatInfos.[].seatNameType").description(
                                        "예매한 좌석 이름")
                        ))
                );
    }
}