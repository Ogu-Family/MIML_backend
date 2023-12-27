package com.miml.c2k.domain.screen.controller;

import com.miml.c2k.domain.screen.dto.ScreenAdminResponseDto;
import com.miml.c2k.domain.screen.service.ScreenService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScreenAdminController {

    private final ScreenService screenService;

    @GetMapping("/api/v1/screens")
    public List<ScreenAdminResponseDto> getAllScreens() {
        return screenService.getAllScreens();
    }
}
