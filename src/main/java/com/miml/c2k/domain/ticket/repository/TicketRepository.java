package com.miml.c2k.domain.ticket.repository;

import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.ticket.Ticket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = "SELECT t FROM Ticket t WHERE t.member.id = :memberId")
    List<Ticket> findAllByMemberId(Long memberId);

    @Query(value = "SELECT s FROM Schedule s JOIN Ticket t ON s.id = t.schedule.id WHERE t.id = :ticketId")
    Optional<Schedule> findScheduleByTicketId(Long ticketId);
}
