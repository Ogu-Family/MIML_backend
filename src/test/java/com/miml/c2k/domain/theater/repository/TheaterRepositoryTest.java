package com.miml.c2k.domain.theater.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.theater.Theater;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TheaterRepositoryTest {

    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private ScreenRepository screenRepository;

    @Test
    @DisplayName("특정 상영관에 연결된 영화관을 가져온다.")
    void success_find_a_theater_by_screen_id() {
        // given
        Theater theater = Theater.builder().name("theater").build();
        Screen screen = Screen.builder().num(2).seatCount(100).theater(theater).build();

        theaterRepository.save(theater);
        screenRepository.save(screen);

        // when
        Optional<Theater> retrievedTheater = theaterRepository.findByScreenId(screen.getId());

        // then
        assertThat(retrievedTheater.isPresent()).isTrue();
        assertThat(retrievedTheater.get().getId()).isEqualTo(theater.getId());
    }
}
