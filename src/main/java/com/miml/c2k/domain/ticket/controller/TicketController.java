package com.miml.c2k.domain.ticket.controller;

import com.miml.c2k.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @DeleteMapping("/api/v1/ticket/{ticketId}")
    @ResponseBody
    public void cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
    }
}
