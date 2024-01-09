package com.miml.c2k.domain.ticket.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import com.miml.c2k.domain.ticket.Ticket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("member id로 특정 회원의 티켓들을 가져온다.")
    void success_find_all_tickets_by_member_id() {
        // given
        Member member = Member.builder().nickname("nick").email("cho@gmail.com").build();
        Ticket ticket1 = Ticket.builder().member(member).build();
        Ticket ticket2 = Ticket.builder().member(member).build();
        Seat seat1 = Seat.builder().name(SeatNameType.J10).build();
        Seat seat2 = Seat.builder().name(SeatNameType.J10).build();

        ticket1.addSeat(seat1);
        ticket2.addSeat(seat2);

        memberRepository.save(member);
        ticketRepository.saveAll(List.of(ticket1, ticket2));
        seatRepository.saveAll(List.of(seat1, seat2));

        // when
        List<Ticket> tickets = ticketRepository.findAllByMemberId(member.getId());

        // then
        assertThat(tickets).hasSize(2);
    }

    @Test
    @DisplayName("특정 티켓과 연결된 상영 일정을 가져온다.")
    void success_find_a_schedule_by_ticket_id() {
        // given
        Schedule schedule = Schedule.builder().startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now()).build();
        Ticket ticket = Ticket.builder().schedule(schedule).build();

        scheduleRepository.save(schedule);
        ticketRepository.save(ticket);

        // when
        Optional<Schedule> retrievedSchedule = ticketRepository.findScheduleByTicketId(ticket.getId());

        // then
        assertThat(retrievedSchedule.isPresent()).isTrue();
        assertThat(retrievedSchedule.get().getId()).isEqualTo(schedule.getId());
    }

    @Test
    @Disabled // TODO: 결제 구현 후 작성
    void success_find_payment_by_ticket_id() {
    }
}
