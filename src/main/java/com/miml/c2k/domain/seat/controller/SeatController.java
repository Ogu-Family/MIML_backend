package com.miml.c2k.domain.seat.controller;

import com.miml.c2k.domain.member.dto.MemberResponseDto;
import com.miml.c2k.domain.member.service.MemberService;
import com.miml.c2k.domain.seat.dto.ReservedSeatResponseDto;
import com.miml.c2k.domain.seat.dto.SeatRequestDto;
import com.miml.c2k.domain.seat.service.SeatService;
import com.miml.c2k.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;
    private final MemberService memberService;

    @GetMapping("/seats")
    public String getAllSeats(
            @RequestParam("schedule_id") Long scheduleId,
            @RequestParam("seat_count") Long seatCount, Model model) {
        model.addAttribute("seats", seatService.getAllSeats(scheduleId));
        model.addAttribute("seat_count", seatCount);
        return "schedule/select-seat";
    }

    @PostMapping("/api/v1/seats")
    @ResponseBody
    public ApiResponse<ReservedSeatResponseDto> reserveSeats(
            @RequestHeader(name = "accessToken") String accessToken,
            @RequestBody SeatRequestDto seatRequestDto) {
        MemberResponseDto memberResponseDto = memberService.findMemberByAccessToken(accessToken);

        return ApiResponse.create(HttpStatus.SC_OK, "좌석 예매 성공",
                seatService.reserveSeat(seatRequestDto, memberResponseDto.getId()));
    }
}