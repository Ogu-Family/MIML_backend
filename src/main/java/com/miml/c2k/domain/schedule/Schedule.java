package com.miml.c2k.domain.schedule;

import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.screen.Screen;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {
    private static final int EARLY_MORNING_LIMIT = 10;
    private static final int EARLY_MORNING_FEE = 8_000;
    private static final int NORMAL_FEE = 12_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "fee", nullable = false, columnDefinition = "int default 0")
    private Integer fee;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public Schedule(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = calculateFee();
    }

    public void updateScreen(Screen screen) {
        this.screen = screen;
    }

    public void updateMovie(Movie movie) {
        this.movie = movie;
    }

    private int calculateFee() {
        return this.startTime.getHour() < EARLY_MORNING_LIMIT ? EARLY_MORNING_FEE : NORMAL_FEE;
    }
}
