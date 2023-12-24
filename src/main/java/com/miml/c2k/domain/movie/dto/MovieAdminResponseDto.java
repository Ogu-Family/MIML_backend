package com.miml.c2k.domain.movie.dto;

import com.miml.c2k.domain.movie.Movie;
import lombok.Getter;

@Getter
public class MovieAdminResponseDto {
    private final Long id;
    private final String title;
    private final String director;
    private final String poster;

    private MovieAdminResponseDto(Long id, String title, String director, String poster) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.poster = poster;
    }

    public static MovieAdminResponseDto create(Movie movie) {
        return new MovieAdminResponseDto(movie.getId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getPoster());
    }
}
