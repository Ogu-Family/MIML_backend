package com.miml.c2k.domain.movie;

import com.miml.c2k.domain.movie.dto.MovieResponseDto;
import com.miml.c2k.domain.movie.dto.PlayingStatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long audienceCount = 0L;

    @Column(name = "code", nullable = false, unique = true)
    private Long code;

    public Movie(String title, String director, String genre, String nation, Long code) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.nation = nation;
        this.code = code;
    }

    public void updateAudienceCount(Long audienceCount) {
        this.audienceCount = audienceCount;
    }

    public MovieResponseDto toMovieResponseDto(PlayingStatusType playingStatus) {
        return new MovieResponseDto(this.id, this.title, this.director, this.genre, this.nation,
                this.poster, this.audienceCount, this.code, playingStatus);
    }
}
