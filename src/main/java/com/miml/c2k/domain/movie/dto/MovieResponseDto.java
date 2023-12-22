package com.miml.c2k.domain.movie.dto;

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
}
