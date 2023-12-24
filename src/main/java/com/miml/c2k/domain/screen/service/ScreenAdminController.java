package com.miml.c2k.domain.screen.service;

import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.dto.ScreenAdminResponseDto;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScreenAdminController {

    private final ScreenRepository screenRepository;

    @GetMapping("/api/v1/screens")
    public List<ScreenAdminResponseDto> getAllScreen() {
        List<Screen> screens = screenRepository.findAll();

        return screens.stream().map(ScreenAdminResponseDto::create).toList();
    }
}
