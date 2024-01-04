package com.miml.c2k.domain.ticket;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import jakarta.persistence.Column;
import com.miml.c2k.domain.seat.Seat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Ticket {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
  
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
  
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
  
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private final List<Seat> seats = new ArrayList<>();

    private Ticket(Member member, Schedule schedule, Payment payment) {
        this.status = TicketStatus.ACTIVE; // TODO: 결제 구현 후에는, BEFORE_PAYMENT 상태가 기본 값이 되도록 해야 함.
        this.member = member;
        this.schedule = schedule;
        this.payment = payment;
    }

    public static Ticket createWithoutPayment(Member member, Schedule schedule) {
        return new Ticket(member, schedule, null);
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setTicket(this);
    }
  
    public void changeStatusCorrectly() {
        if (isExpired()) {
            status = TicketStatus.INACTIVE;
        }
    }

    public void cancel() {
        this.status = TicketStatus.CANCELED;
    }

    private boolean isExpired() {
        return (status == TicketStatus.ACTIVE || status == TicketStatus.BEFORE_PAYMENT)
            && schedule.getStartTime().isBefore(LocalDateTime.now());
    }
}
