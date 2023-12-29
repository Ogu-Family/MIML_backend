package com.miml.c2k.domain.member.controller;

import com.miml.c2k.domain.member.dto.MemberResponseDto;
import com.miml.c2k.domain.member.dto.MemberUpdateDto;
import com.miml.c2k.domain.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public ResponseEntity<List<MemberResponseDto>> findAllMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }

    @GetMapping("/api/v1/myPage/{accessToken}")
    public String myPage(@PathVariable String accessToken, Model model) {
        MemberResponseDto memberResponseDto = memberService.findMemberByAccessToken(accessToken);

        model.addAttribute("memberResponseDto", memberResponseDto);

        return "/myPage/myPage";
    }

    @PutMapping("/api/v1/myPage/{accessToken}")
    public ResponseEntity<MemberResponseDto> update(@PathVariable String accessToken, @RequestBody MemberUpdateDto updateMemberDto) {
        MemberResponseDto memberResponseDto = memberService.updateMember(accessToken, updateMemberDto);

        return ResponseEntity.ok(memberResponseDto);
    }

}
