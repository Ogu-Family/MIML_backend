package com.miml.c2k.domain.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    private static final int EARLY_MORNING_FEE = 8_000;
    private static final int NORMAL_FEE = 12_000;

    @Test
    @DisplayName("상영 일정이 시간 대에 맞는 적절한 금액으로 책정된다.")
    void success_making_schedule_to_have_correct_fee() {
        Schedule earlyMorningSchedule = Schedule.builder()
            .startTime(LocalDateTime.of(2024, 1, 1, 9, 59))
            .build();
        Schedule justSchedule = Schedule.builder()
            .startTime(LocalDateTime.of(2024, 1, 1, 10, 0))
            .build();

        assertThat(earlyMorningSchedule.getFee()).isEqualTo(EARLY_MORNING_FEE);
        assertThat(justSchedule.getFee()).isEqualTo(NORMAL_FEE);
    }
}
