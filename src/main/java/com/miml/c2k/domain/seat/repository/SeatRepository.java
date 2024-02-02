package com.miml.c2k.domain.seat.repository;

import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(value = "SELECT count(se) FROM Seat se JOIN Ticket t ON se.ticket.id = t.id WHERE t.schedule.id = :scheduleId")
    int countReservedSeatsByScheduleId(Long scheduleId);

    @Query("SELECT se FROM Seat se WHERE se.schedule.id = :scheduleId AND se.ticket IS NOT NULL")
    List<Seat> findAllReservedSeatsByScheduleId(Long scheduleId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Seat> findAllByScheduleIdAndNameIn(Long scheduleId, Collection<SeatNameType> names);
}
