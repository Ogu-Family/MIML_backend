package com.miml.c2k.domain.movie.controller;

import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createMoviesWillPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createSchedules;
import static com.miml.c2k.domain.DataFactoryUtil.createScreens;
import static com.miml.c2k.domain.DataFactoryUtil.createTheaters;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.miml.c2k.domain.movie.dto.PlayingStatusType;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
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
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private TheaterRepository theaterRepository;

    @BeforeEach
    void setUp() {
        Screen screen = screenRepository.saveAll(
                createScreens(theaterRepository.saveAll(createTheaters()))).get(0);
        scheduleRepository.saveAll(
                createSchedules(movieRepository.saveAll(createMoviesIsPlaying(1)),
                        screen));
        movieRepository.saveAll(createMoviesWillPlaying(1));
    }

    @AfterEach
    void clear() {
        scheduleRepository.deleteAll();
        movieRepository.deleteAll();
        screenRepository.deleteAll();
        theaterRepository.deleteAll();
    }

    @Test
    @DisplayName("현재 상영 중인 영화 목록 뷰를 요청한다.")
    void success_getMovieViewByPlayingStatus_given_isPlaying() throws Exception {
        // given
        String playingStatus = "is_playing";

        // when
        ResultActions perform = mockMvc.perform(
                get("/movies").param("playing_status", playingStatus));

        // then
        perform.andExpect(view().name("member/movie-list"))
                .andExpect(model().attribute("movies", hasSize(1)))
                .andExpect(model().attribute("playingStatusTitle",
                        PlayingStatusType.valueOf(playingStatus.toUpperCase()).getName()));
    }

    @Test
    @DisplayName("상영 예정 영화 목록 뷰를 요청한다.")
    void success_getMovieViewByPlayingStatus_given_willPlaying() throws Exception {
        // given
        String playingStatus = "will_playing";

        // when
        ResultActions perform = mockMvc.perform(
                get("/movies").param("playing_status", playingStatus));

        // then
        perform.andExpect(view().name("member/movie-list"))
                .andExpect(model().attribute("movies", hasSize(1)))
                .andExpect(model().attribute("playingStatusTitle",
                        PlayingStatusType.valueOf(playingStatus.toUpperCase()).getName()));
    }

    @Test
    @DisplayName("현재 상영 중인 영화 목록을 요청한다.")
    void success_getMoviesByPlayingStatus_given_isPlaying() throws Exception {
        // given
        String playingStatus = "is_playing";

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/movies")
                .param("playing_status", playingStatus)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("영화 목록 정상 반환"))
                .andExpect(jsonPath("$.data.size()").value(1))
                .andDo(document("getMoviesIsPlaying",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("message").description("성공 메시지"),
                                fieldWithPath("data.[].id").description("영화 아이디"),
                                fieldWithPath("data.[].title").description("영화 제목"),
                                fieldWithPath("data.[].director").description("영화 감독"),
                                fieldWithPath("data.[].genre").description("게시글 장르"),
                                fieldWithPath("data.[].nation").description("제작 국가"),
                                fieldWithPath("data.[].poster").description("영화 포스터"),
                                fieldWithPath("data.[].audienceCount").description("누적 관객 수"),
                                fieldWithPath("data.[].code").description("영화 코드"),
                                fieldWithPath("data.[].playingStatus").description("상영 상태")
                        ))
                );
    }

    @Test
    @DisplayName("상영 예정 영화 목록을 요청한다.")
    void success_getMoviesByPlayingStatus_given_willPlaying() throws Exception {
        // given
        String playingStatus = "will_playing";

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/movies")
                .param("playing_status", playingStatus)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("영화 목록 정상 반환"))
                .andExpect(jsonPath("$.data.size()").value(1))
                .andDo(document("getMoviesWillPlaying",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("message").description("성공 메시지"),
                                fieldWithPath("data.[].id").description("영화 아이디"),
                                fieldWithPath("data.[].title").description("영화 제목"),
                                fieldWithPath("data.[].director").description("영화 감독"),
                                fieldWithPath("data.[].genre").description("게시글 장르"),
                                fieldWithPath("data.[].nation").description("제작 국가"),
                                fieldWithPath("data.[].poster").description("영화 포스터"),
                                fieldWithPath("data.[].audienceCount").description("누적 관객 수"),
                                fieldWithPath("data.[].code").description("영화 코드"),
                                fieldWithPath("data.[].playingStatus").description("상영 상태")
                        ))
                );
    }
}