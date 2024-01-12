package com.miml.c2k.domain.member.dto;

import java.util.regex.Pattern;

public record MemberUpdateDto(String nickname) {

    private static final int MAX_NICKNAME_LENGTH = 20;
    private static final String regex = "^[가-힣a-zA-Z]*$";

    public MemberUpdateDto {
        if (nickname == null || nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new IllegalArgumentException("닉네임은 20글자를 넘길 수 없습니다.");
        }
        if (!Pattern.matches(regex, nickname)) {
            throw new IllegalArgumentException("닉네임은 한글과 영어로만 이루어져야 합니다.");
        }
    }

}
