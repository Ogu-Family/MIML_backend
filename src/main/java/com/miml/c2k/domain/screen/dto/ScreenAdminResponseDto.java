package com.miml.c2k.domain.screen.dto;

import com.miml.c2k.domain.screen.Screen;

public class ScreenAdminResponseDto {

    private Long id;
    private int num;
    private Long theaterId;

    private ScreenAdminResponseDto(Long id, int num, Long theaterId) {
        this.id = id;
        this.num = num;
        this.theaterId = theaterId;
    }

    public static ScreenAdminResponseDto create(Screen screen) {
        return new ScreenAdminResponseDto(screen.getId(),
            screen.getNum(),
            screen.getTheater().getId());
    }
}
