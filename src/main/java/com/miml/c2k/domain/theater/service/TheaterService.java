package com.miml.c2k.domain.theater.service;

import com.miml.c2k.domain.theater.Theater;
import com.miml.c2k.domain.theater.dto.TheaterAdminResponseDto;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public List<TheaterAdminResponseDto> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findAll();

        return theaters.stream().map(TheaterAdminResponseDto::create).toList();
    }
}
