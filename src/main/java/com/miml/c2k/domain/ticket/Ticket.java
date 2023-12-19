package com.miml.c2k.domain.ticket;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.payment.Payment;
import com.miml.c2k.domain.schedule.Schedule;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Schedule schedule;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
