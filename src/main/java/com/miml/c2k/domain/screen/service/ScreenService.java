package com.miml.c2k.domain.screen.service;

import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.dto.ScreenAdminResponseDto;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;

    public List<ScreenAdminResponseDto> getAllScreens() {
        List<Screen> screens = screenRepository.findAll();

        return screens.stream().map(ScreenAdminResponseDto::create).toList();
    }
}
