package com.miml.c2k.domain.member.service;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.dto.MemberResponseDto;
import com.miml.c2k.domain.member.dto.MemberUpdateDto;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.global.auth.jwt.AuthTokensGenerator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;

    public List<MemberResponseDto> findAllMembers() {
        return memberRepository.findAll().stream()
                .map(member -> MemberResponseDto.builder()
                        .nickname(member.getNickname())
                        .email(member.getEmail())
                        .oAuthProvider(member.getOAuthProvider())
                        .createdAt(member.getCreatedAt())
                        .build())
                .toList();
    }

    public MemberResponseDto findMemberByAccessToken(String accessToken) {
        Member member = findMember(accessToken);

        return MemberResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .oAuthProvider(member.getOAuthProvider())
                .createdAt(member.getCreatedAt())
                .build();
    }

    @Transactional
    public MemberResponseDto updateMember(String accessToken, MemberUpdateDto memberUpdateDto) {
        Member member = findMember(accessToken);

        member.update(memberUpdateDto.nickname());

        return MemberResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .oAuthProvider(member.getOAuthProvider())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member findMember(String accessToken) {
        return memberRepository.findById(authTokensGenerator.extractMemberId(accessToken))
                .orElseThrow(() -> new EntityNotFoundException("찾지 못함")); //To DO: 커스텀화
    }
}
