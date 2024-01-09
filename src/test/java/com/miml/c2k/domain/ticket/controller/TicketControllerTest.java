package com.miml.c2k.domain.ticket.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.miml.c2k.domain.ticket.service.TicketService;
import com.miml.c2k.global.auth.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TicketController.class)
@Import({SecurityConfig.class})
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    @DisplayName("티켓 취소 API 동작을 테스트한다.")
    void success_request_cancel_ticket() throws Exception {
        Long ticketId = 1L;

        mockMvc.perform(delete("/api/v1/ticket/{ticketId}", ticketId))
            .andExpect(status().isOk());

        verify(ticketService).cancelTicket(ticketId);
    }
}
