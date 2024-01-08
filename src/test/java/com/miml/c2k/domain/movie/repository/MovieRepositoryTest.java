package com.miml.c2k.domain.movie.repository;

import static com.miml.c2k.domain.DataFactoryUtil.createMoviesIsPlaying;
import static com.miml.c2k.domain.DataFactoryUtil.createMoviesWillPlaying;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.miml.c2k.domain.movie.Movie;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("현재 날짜 이후 개봉하는 영화 목록을 가져온다.")
    void success_findMoviesOpeningAfterCurrentDate() {
        // given
        movieRepository.saveAll(createMoviesIsPlaying(10));
        movieRepository.saveAll(createMoviesWillPlaying(10));

        // when
        List<Movie> moviesOpeningAfterCurrentDate = movieRepository.findMoviesOpeningAfterCurrentDate(
                LocalDate.now());

        // then: 추출한 영화의 개봉일자가 현재일자 이후인지 확인
        assertEquals(10, moviesOpeningAfterCurrentDate.size());
        moviesOpeningAfterCurrentDate.stream()
                .forEach(movie -> assertThat(movie.getOpenDate(), greaterThan(LocalDate.now())));
    }
}