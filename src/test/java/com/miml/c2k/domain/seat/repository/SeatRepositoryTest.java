package com.miml.c2k.domain.seat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.ticket.Ticket;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // TODO: 이 부분 상의
class SeatRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("상영 일자 id를 이용해 해당 상영 일정의 예약된 좌석 수를 가져온다.")
    void success_count_reserved_seats_by_schedule_id() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);

        Schedule savedSchedule = scheduleRepository.save(
            Schedule.builder().startTime(startTime).endTime(endTime).build());

        Ticket ticket1 = Ticket.builder().schedule(savedSchedule).build();
        Ticket ticket2 = Ticket.builder().schedule(savedSchedule).build();

        Seat seat1 = Seat.builder().name(SeatNameType.J10).build();
        Seat seat2 = Seat.builder().name(SeatNameType.J11).build();
        Seat seat3 = Seat.builder().name(SeatNameType.J12).build();

        ticket1.addSeat(seat1);
        ticket1.addSeat(seat2);
        ticket2.addSeat(seat3);

        ticketRepository.saveAll(List.of(ticket1, ticket2));

        // when
        int reservedSeatCount = seatRepository.countReservedSeatsByScheduleId(savedSchedule.getId());

        // then
        assertThat(reservedSeatCount).isEqualTo(3);
    }
}
