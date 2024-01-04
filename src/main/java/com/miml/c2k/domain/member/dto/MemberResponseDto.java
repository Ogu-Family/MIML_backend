package com.miml.c2k.domain.member.dto;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.OAuthProvider;
import com.miml.c2k.domain.member.Role;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private final Long id;
    private final String nickname;
    private final String email;
    private final OAuthProvider oAuthProvider;
    private final Role role;
    private final LocalDateTime createdAt;

    @Builder
    private MemberResponseDto(Long id, String nickname, String email, OAuthProvider oAuthProvider, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.oAuthProvider = oAuthProvider;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static MemberResponseDto create(Member member) {
        return MemberResponseDto.builder()
            .id(member.getId())
            .nickname(member.getNickname())
            .email(member.getEmail())
            .oAuthProvider(member.getOAuthProvider())
            .role(member.getRole())
            .createdAt(member.getCreatedAt())
            .build();
    }
}
