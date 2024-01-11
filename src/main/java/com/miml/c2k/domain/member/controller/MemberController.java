package com.miml.c2k.domain.member.controller;

import com.miml.c2k.domain.member.dto.MemberResponseDto;
import com.miml.c2k.domain.member.dto.MemberUpdateDto;
import com.miml.c2k.domain.member.service.MemberService;
import com.miml.c2k.domain.ticket.dto.TicketInfoResponseDto;
import com.miml.c2k.domain.ticket.service.TicketService;
import com.miml.c2k.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TicketService ticketService;

    @ResponseBody
    @GetMapping("/api/v1/members")
    public ApiResponse<MemberResponseDto> findMember(
            @RequestHeader(name = "accessToken") String accessToken) {
        return ApiResponse.create(HttpStatus.OK.value(), "정상 처리",
                memberService.findMemberByAccessToken(accessToken));
    }

    @GetMapping("/myPage")
    public String showMyPage(@RequestHeader(name = "accessToken") String accessToken, Model model) {
        MemberResponseDto memberResponseDto = memberService.findMemberByAccessToken(accessToken);

        List<TicketInfoResponseDto> ticketInfoResponseDtos = ticketService.getAllTicketsInfoByMemberId(
            memberResponseDto.getId());

        model.addAttribute("memberResponseDto", memberResponseDto);
        model.addAttribute("ticketInfoResponseDtos", ticketInfoResponseDtos);

        return "/myPage/myPage";
    }

    @ResponseBody
    @PutMapping("/api/v1/myPage")
    public ApiResponse<MemberResponseDto> update(
            @RequestHeader(name = "accessToken") String accessToken,
            @RequestBody MemberUpdateDto updateMemberDto) {
        return ApiResponse.create(HttpStatus.OK.value(), "정상 처리",
                memberService.updateMember(accessToken, updateMemberDto));
    }

}
