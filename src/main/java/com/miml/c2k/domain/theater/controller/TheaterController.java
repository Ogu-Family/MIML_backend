package com.miml.c2k.domain.theater.controller;

import com.miml.c2k.domain.theater.dto.TheaterAdminResponseDto;
import com.miml.c2k.domain.theater.service.TheaterService;
import com.miml.c2k.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    @GetMapping("/api/v1/theaters")
    public ApiResponse<List<TheaterAdminResponseDto>> getAllTheaters() {
        return ApiResponse.create(HttpStatus.SC_OK, "영화관 목록 정상 반환", theaterService.getAllTheaters());
    }
}
