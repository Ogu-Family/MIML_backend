package com.miml.c2k.domain.movie.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayingStatusTypeTest {

    @Test
    @DisplayName("문자열에 해당하는 상영 상태 값을 반환한다.")
    void success_getPlayingStatus() {
        // given
        String playingStatus = "is_playing";

        // when
        PlayingStatusType value = PlayingStatusType.getValue(playingStatus);

        // then
        assertEquals(PlayingStatusType.IS_PLAYING, value);
    }

    @Test
    @DisplayName("유효하지 않은 상영 상태에 대해 예외를 던진다.")
    void fail_getPlayingStatus_given_invalidStatus() {
        // given
        String playingStatus = "invalid_playing_status";

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PlayingStatusType.getValue(playingStatus);
        });

        // then
        assertEquals("유효하지 않은 상영 상태 값 입니다.", exception.getMessage());
    }
}