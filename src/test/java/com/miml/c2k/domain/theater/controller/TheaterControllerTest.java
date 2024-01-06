package com.miml.c2k.domain.theater.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.miml.c2k.domain.theater.Theater;
import com.miml.c2k.domain.theater.dto.TheaterAdminResponseDto;
import com.miml.c2k.domain.theater.service.TheaterService;
import com.miml.c2k.global.auth.SecurityConfig;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TheaterController.class)
@AutoConfigureRestDocs
@Import(SecurityConfig.class)
class TheaterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TheaterService theaterService;

    @Test
    @DisplayName("모든 영화관 목록을 반환한다.")
    void success_get_all_theaters() throws Exception {
        // given
        Field theaterIdField = Theater.class.getDeclaredField("id");
        theaterIdField.setAccessible(true);
        long idValue = 1L;

        Theater theater1 = Theater.builder().name("1").build();
        Theater theater2 = Theater.builder().name("2").build();
        List<Theater> theaters = List.of(theater1, theater2);

        for (Theater theater : theaters) {
            theaterIdField.set(theater, idValue++);
        }

        List<TheaterAdminResponseDto> theaterAdminResponseDtos = theaters.stream()
            .map(TheaterAdminResponseDto::create).toList();

        when(theaterService.getAllTheaters()).thenReturn(theaterAdminResponseDtos);

        // when
        mockMvc.perform(get("/api/v1/theaters").characterEncoding(StandardCharsets.UTF_8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("영화관 목록 정상 반환"))
            .andExpect(jsonPath("$.data.size()").value(2))
            .andDo(print())
            .andDo(document("getAllTheaters", preprocessResponse(prettyPrint()), responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("처리 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("처리 메시지"),
                fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("영화관 목록"),
                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("영화관 id"),
                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("영화관 이름")
            )));

        // then
        verify(theaterService).getAllTheaters();
    }
}
