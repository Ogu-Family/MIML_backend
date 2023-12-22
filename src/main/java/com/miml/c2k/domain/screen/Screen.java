package com.miml.c2k.domain.screen;

import com.miml.c2k.domain.theater.Theater;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num", nullable = false)
    private Integer num;

    @Column(name = "seat_cnt", nullable = false, columnDefinition = "int default 0")
    private Integer seatCnt;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;
}
