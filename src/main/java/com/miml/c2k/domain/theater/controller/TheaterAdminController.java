package com.miml.c2k.domain.theater.controller;

import com.miml.c2k.domain.theater.dto.TheaterAdminResponseDto;
import com.miml.c2k.domain.theater.service.TheaterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TheaterAdminController {

    private final TheaterService theaterService;

    @GetMapping("/api/v1/theaters")
    public List<TheaterAdminResponseDto> getAllTheaters() {
        return theaterService.getAllTheaters();
    }
}
