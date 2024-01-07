package com.miml.c2k.domain.screen;

import com.miml.c2k.domain.theater.Theater;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num", nullable = false)
    private Integer num;

    @Column(name = "seat_count", nullable = false, columnDefinition = "int default 0")
    private Integer seatCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Builder
    public Screen(Integer num, Integer seatCount, Theater theater) {
        this.num = num;
        this.seatCount = seatCount;
        this.theater = theater;
    }
}
