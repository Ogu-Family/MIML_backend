package com.miml.c2k.domain.member.dto;

import com.miml.c2k.domain.member.OAuthProvider;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private final String nickname;
    private final String email;
    private final OAuthProvider oAuthProvider;
    private final LocalDateTime createdAt;

    @Builder
    public MemberResponseDto(String nickname, String email, OAuthProvider oAuthProvider, LocalDateTime createdAt) {
        this.nickname = nickname;
        this.email = email;
        this.oAuthProvider = oAuthProvider;
        this.createdAt = createdAt;
    }

}
