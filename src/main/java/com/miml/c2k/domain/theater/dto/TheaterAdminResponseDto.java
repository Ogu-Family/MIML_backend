package com.miml.c2k.domain.theater.dto;

import com.miml.c2k.domain.theater.Theater;

public class TheaterAdminResponseDto {

    private Long id;
    private String name;

    private TheaterAdminResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TheaterAdminResponseDto create(Theater theater) {
        return new TheaterAdminResponseDto(theater.getId(), theater.getName());
    }
}
