package com.miml.c2k.domain.screen.dto;

import com.miml.c2k.domain.screen.Screen;
import lombok.Getter;

@Getter
public class ScreenAdminResponseDto {

    private final Long id;
    private final int num;
    private final Long theaterId;

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
