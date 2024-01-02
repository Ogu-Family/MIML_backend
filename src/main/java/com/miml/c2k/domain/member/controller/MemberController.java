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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public ResponseEntity<List<MemberResponseDto>> findAllMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }

    @GetMapping("/myPage")
    public String myPage(@RequestHeader(name = "accessToken") String accessToken, Model model) {
        MemberResponseDto memberResponseDto = memberService.findMemberByAccessToken(accessToken);

        model.addAttribute("memberResponseDto", memberResponseDto);

        return "/myPage/myPage";
    }

    @PutMapping("/api/v1/myPage") //To Do: 추후에 프론트 뷰 수정해서 리다이렉트 하는 api로 수정
    public ResponseEntity<MemberResponseDto> update(@RequestHeader(name = "accessToken") String accessToken, @RequestBody MemberUpdateDto updateMemberDto) {
        MemberResponseDto memberResponseDto = memberService.updateMember(accessToken, updateMemberDto);

        return ResponseEntity.ok(memberResponseDto);
    }

}
