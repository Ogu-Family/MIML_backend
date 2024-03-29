package com.miml.c2k.domain.movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "director")
    private String director;

    @Column(name = "genre")
    private String genre;

    @Column(name = "nation")
    private String nation;

    @Column(name = "poster", columnDefinition = "text")
    private String poster;

    @Column(name = "audience_count", nullable = false, columnDefinition = "int default 0")
    private long audienceCount = 0L;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Builder
    public Movie(String title, String director, String genre, String nation, long audienceCount, String code,
            LocalDate openDate) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.nation = nation;
        this.audienceCount = audienceCount;
        this.code = code;
        this.openDate = openDate;
    }

    public void updateAudienceCount(Long audienceCount) {
        this.audienceCount = audienceCount;
    }
}
