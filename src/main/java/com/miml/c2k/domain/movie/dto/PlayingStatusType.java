package com.miml.c2k.domain.movie.dto;

public enum PlayingStatusType {

    IS_PLAYING(1, "상영 중"), WILL_PLAYING(2, "상영 예정");

    private final Integer num;
    private final String name;

    PlayingStatusType(Integer num, String name) {
        this.num = num;
        this.name = name;
    }

    public static PlayingStatusType getValueByNum(int num) {
        for (PlayingStatusType playingStatusType : PlayingStatusType.values()) {
            if (playingStatusType.num == num) {
                return playingStatusType;
            }
        }

        throw new IllegalArgumentException("예외 처리 필요");
    }

    public String getName() {
        return name;
    }
}
