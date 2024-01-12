package com.miml.c2k.domain.member;

import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miml.c2k.domain.member.dto.MemberResponseDto;
import com.miml.c2k.domain.member.dto.MemberUpdateDto;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.member.service.MemberService;
import com.miml.c2k.domain.ticket.dto.TicketInfoResponseDto;
import com.miml.c2k.domain.ticket.service.TicketService;
import com.miml.c2k.global.auth.jwt.AuthTokensGenerator;
import java.util.List;
import org.hamcrest.Matchers;
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
class MemberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    private String accessToken;

    @BeforeEach
    void setup() {
        Member member = memberRepository.save(new Member("kim", OAuthProvider.KAKAO, "kim@gmail.com"));
        accessToken = authTokensGenerator.generate(member.getId()).getAccessToken();
    }

    @AfterEach
    void deleteAll() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("AccessToken을 통해 회원을 조회할 수 있다.")
    void success_find_member_by_accessToken() throws Exception {

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/members")
                .header("accessToken", accessToken));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("정상 처리"))
                .andDo(document("memberResponseDto",
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("처리 메시지"),
                                fieldWithPath("data.id").description("회원 번호"),
                                fieldWithPath("data.nickname").description("닉네임"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.oauthProvider").description("OAuth 제공사"),
                                fieldWithPath("data.role").description("역할"),
                                fieldWithPath("data.createdAt").description("생성 시간")
                        ))
                );
    }

    @Test
    @DisplayName("AccessToken을 통해 마이페이지 뷰에 접근할 수 있다.")
    void success_access_myPage_by_accessToken() throws Exception {

        //given
        MemberResponseDto memberResponseDto = memberService.findMemberByAccessToken(accessToken);
        List<TicketInfoResponseDto> allTicketsInfoByMemberId = ticketService.getAllTicketsInfoByMemberId(memberResponseDto.getId());

        // when
        ResultActions resultActions = mockMvc.perform(get("/myPage")
                .header("accessToken", accessToken));

        // then
        resultActions.andExpect(view().name("/myPage/myPage"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("memberResponseDto", hasProperty("nickname",
                        Matchers.equalTo(memberResponseDto.getNickname()))))
                .andExpect(model().attribute("ticketInfoResponseDtos", allTicketsInfoByMemberId));
    }

    @Test
    @DisplayName("AccessToken을 통해 Member의 nickname을 update할 수 있다.")
    void success_update_nickname_by_accessToken() throws Exception {

        //given
        MemberUpdateDto updateMemberDto = new MemberUpdateDto("닉네임변경쓰");

        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/myPage")
                .header("accessToken", accessToken)
                .content(new ObjectMapper().writeValueAsString(updateMemberDto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("정상 처리"))
                .andDo(document("memberResponseDto",
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("처리 메시지"),
                                fieldWithPath("data.id").description("회원 번호"),
                                fieldWithPath("data.nickname").description("닉네임"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.oauthProvider").description("OAuth 제공사"),
                                fieldWithPath("data.role").description("역할"),
                                fieldWithPath("data.createdAt").description("생성 시간")
                        ))
                );
    }
}
