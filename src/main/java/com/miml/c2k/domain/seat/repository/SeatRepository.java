package com.miml.c2k.domain.seat.repository;

import com.miml.c2k.domain.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query(value = "SELECT count(s) FROM Seat s JOIN Ticket t ON s.ticket.id = t.id WHERE t.schedule.id = :scheduleId")
    int countReservedSeatsByScheduleId(Long scheduleId);
}
