package com.miml.c2k.domain.ticket.repository;

import com.miml.c2k.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
