package com.miml.c2k.domain.ticket;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.seat.Seat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Ticket {

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private final List<Seat> seats = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private Ticket(Member member, Schedule schedule, Payment payment) {
        this.member = member;
        this.schedule = schedule;
        this.payment = payment;
    }

    public static Ticket createWithoutPayment(Member member, Schedule schedule) {
        return new Ticket(member, schedule, null);
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }
}
