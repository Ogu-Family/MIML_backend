package com.miml.c2k.domain.movie.dto;

public enum PlayingStatusType {

    IS_PLAYING("상영 중"), WILL_PLAYING("상영 예정");
    private final String name;

    PlayingStatusType(String name) {
        this.name = name;
    }

    public static PlayingStatusType getValue(String playingStatus) {
        try {
            return PlayingStatusType.valueOf(playingStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 상영 상태 값 입니다.");
        }
    }

    public String getName() {
        return name;
    }
}
