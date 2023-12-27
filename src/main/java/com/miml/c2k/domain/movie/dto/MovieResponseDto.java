package com.miml.c2k.domain.movie.dto;

import com.miml.c2k.domain.movie.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieResponseDto {

    private final Long id;
    private final String title;
    private final String director;
    private final String genre;
    private final String nation;
    private final String poster;
    private final Long audienceCount;
    private final Long code;
    private final PlayingStatusType playingStatus;

    private MovieResponseDto(Movie movie, PlayingStatusType playingStatus) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.director = movie.getDirector();
        this.genre = movie.getGenre();
        this.nation = movie.getNation();
        this.poster = movie.getPoster();
        this.audienceCount = movie.getAudienceCount();
        this.code = movie.getCode();
        this.playingStatus = playingStatus;
    }

    public static MovieResponseDto create(Movie movie, PlayingStatusType playingStatus) {
        return new MovieResponseDto(movie, playingStatus);
    }
}
