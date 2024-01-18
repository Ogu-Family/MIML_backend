package com.miml.c2k.domain.ticket.repository;

import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.ticket.Ticket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = "SELECT t FROM Ticket t JOIN FETCH t.seats s WHERE t.member.id = :memberId")
    List<Ticket> findAllByMemberId(Long memberId);

    @Query(value = "SELECT t FROM Ticket t " +
            "JOIN FETCH t.member m " +
            "JOIN FETCH t.schedule s " +
            "JOIN FETCH s.movie " +
            "JOIN FETCH s.screen sc " +
            "JOIN FETCH sc.theater " +
            "LEFT JOIN FETCH t.payment " +
            "JOIN FETCH t.seats " +
            "WHERE t.member.id = :memberId")
    List<Ticket> findAllByMemberIdWithFetchJoin(Long memberId);

    @Query(value = "SELECT sch FROM Schedule sch JOIN Ticket t ON sch.id = t.schedule.id WHERE t.id = :ticketId")
    Optional<Schedule> findScheduleByTicketId(Long ticketId);

    @Query(value = "SELECT p FROM Payment p JOIN Ticket t ON p.id = t.payment.id WHERE t.id = :ticketId")
    Optional<Payment> findPaymentByTicketId(Long ticketId);
}
