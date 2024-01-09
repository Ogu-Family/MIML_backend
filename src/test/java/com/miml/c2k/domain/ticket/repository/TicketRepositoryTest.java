package com.miml.c2k.domain.ticket.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import com.miml.c2k.domain.ticket.Ticket;
import java.util.List;
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
    void findScheduleByTicketId() {
    }

    @Test
    void findPaymentByTicketId() {
    }
}
