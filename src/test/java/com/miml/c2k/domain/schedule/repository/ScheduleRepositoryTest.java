package com.miml.c2k.domain.schedule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ScheduleRepositoryTest {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScreenRepository screenRepository;

    @Test
    @DisplayName("countAllByScreenIdBetweenTimeline 메서드 쿼리로 startTime, endTime 사이 상영 일정 개수를 정확히 셀 수 있다.")
    void success_count_all_by_screen_id_between_timeline() {
        // given
        Screen screen = Screen.builder().seatCount(20).num(1).build();

        Schedule schedule1 = Schedule.builder().screen(screen)
            .startTime(LocalDateTime.of(2024, 1, 8, 21, 0))
            .endTime(LocalDateTime.of(2024, 1, 8, 22, 30)).build();
        Schedule schedule2 = Schedule.builder().screen(screen)
            .startTime(LocalDateTime.of(2024, 1, 8, 22, 50))
            .endTime(LocalDateTime.of(2024, 1, 8, 23, 55)).build();

        screenRepository.save(screen);
        scheduleRepository.saveAll(List.of(schedule1, schedule2));

        // when
        int scheduleCountBetween22To23 = scheduleRepository.countAllByScreenIdBetweenTimeline(schedule1.getId(),
            LocalDateTime.of(2024, 1, 8, 22, 0), LocalDateTime.of(2024, 1, 8, 23, 0));
        int scheduleCountBetween23To24 = scheduleRepository.countAllByScreenIdBetweenTimeline(schedule1.getId(),
            LocalDateTime.of(2024, 1, 8, 23, 0), LocalDateTime.of(2024, 1, 9, 0, 0));

        assertThat(scheduleCountBetween22To23).isEqualTo(2);
        assertThat(scheduleCountBetween23To24).isEqualTo(1);
    }
}
