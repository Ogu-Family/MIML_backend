package com.miml.c2k.domain.movie.service;

import com.miml.c2k.util.KobisOpenApiUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final KobisOpenApiUtil kobisOpenApiUtil;


    public void saveBoxOfficeTop10ByTargetDate(String targetDate) {
        kobisOpenApiUtil.saveBoxOfficeTop10ByTargetDate(targetDate);
    }

    public void saveMovieByMovieCode(String movieCode) {
        kobisOpenApiUtil.getMovieInfoByMovieCodeAndSave(movieCode);
    }
}
