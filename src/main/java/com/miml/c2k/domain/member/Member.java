package com.miml.c2k.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "oAuthProvider")
    private OAuthProvider oAuthProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createdAt;

    @Builder
    public Member(String nickname, OAuthProvider oAuthProvider, String email) {
        this.nickname = nickname;
        this.email = email;
        this.role = Role.USER; // 가입 시 자동으로 user
        this.oAuthProvider = oAuthProvider;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }
}
