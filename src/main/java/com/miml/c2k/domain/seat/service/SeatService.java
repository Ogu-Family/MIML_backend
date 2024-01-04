package com.miml.c2k.domain.seat.service;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.schedule.Schedule;
import com.miml.c2k.domain.schedule.repository.ScheduleRepository;
import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.dto.ReservedSeatResponseDto;
import com.miml.c2k.domain.seat.dto.SeatRequestDto;
import com.miml.c2k.domain.seat.dto.SeatResponseDto;
import com.miml.c2k.domain.seat.repository.SeatRepository;
import com.miml.c2k.domain.ticket.Ticket;
import com.miml.c2k.domain.ticket.repository.TicketRepository;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;

    public List<SeatResponseDto> getAllSeats(Long scheduleId) {

        // 예약된 좌석을 가져와, 예약되지 않은 좌석을 구한다.
        List<Seat> reservedSeats = seatRepository.findAllReservedSeatsByScheduleId(scheduleId);
        Set<SeatNameType> reservedSeatNameTypes = getReservedSeatNameTypes(reservedSeats);
        Set<SeatNameType> unReservedSeatNameTypes = getUnreservedSeatNameTypes(
                reservedSeatNameTypes);

        // 예약된 좌석과 예약되지 않은 좌석에 대한 ResponseDto를 반환한다.
        return getSortedSeatResponseDtos(scheduleId, reservedSeatNameTypes,
                unReservedSeatNameTypes);
    }

    private Set<SeatNameType> getReservedSeatNameTypes(List<Seat> reservedSeats) {
        return reservedSeats.stream().map(Seat::getName).collect(Collectors.toSet());
    }

    private Set<SeatNameType> getUnreservedSeatNameTypes(Set<SeatNameType> reservedSeatNameTypes) {
        Set<SeatNameType> allSeatNameTypes = Arrays.stream(SeatNameType.values())
                .collect(Collectors.toSet());
        allSeatNameTypes.removeAll(reservedSeatNameTypes);
        return allSeatNameTypes;
    }

    private List<SeatResponseDto> getSortedSeatResponseDtos(Long scheduleId,
            Set<SeatNameType> reservedSeatNameTypes, Set<SeatNameType> unReservedSeatNameTypes) {
        return Stream.concat(
                        createSeatResponseDtos(scheduleId, reservedSeatNameTypes, true).stream(),
                        createSeatResponseDtos(scheduleId, unReservedSeatNameTypes, false).stream())
                .sorted(Comparator.comparing(seatDto -> seatDto.getSeatNameType().name()))
                .toList();
    }

    private List<SeatResponseDto> createSeatResponseDtos(Long scheduleId,
            Set<SeatNameType> seatNameTypes, boolean isReserved) {
        return seatNameTypes.stream()
                .map(seatNameType -> SeatResponseDto.builder()
                        .scheduleId(scheduleId)
                        .seatNameType(seatNameType)
                        .isReserved(isReserved)
                        .build())
                .toList();
    }


    public ReservedSeatResponseDto reserveSeat(SeatRequestDto seatRequestDto, Long memberId) {
        Schedule schedule = scheduleRepository.findById(seatRequestDto.getScheduleId())
                .orElseThrow(() -> new RuntimeException("해당 좌석 없음."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 회원"));
        Ticket ticket = ticketRepository.save(Ticket.createWithoutPayment(member, schedule));

        List<Seat> reservedSeats = seatRequestDto.getSeatNameTypes().stream()
                .map(seatNameType -> seatRepository.save(
                        Seat.builder()
                                .name(seatNameType)
                                .screen(schedule.getScreen())
                                .ticket(ticket)
                                .build()))
                .collect(Collectors.toList());
        reservedSeats.forEach(ticket::addSeat);

        return ReservedSeatResponseDto.create(ticket.getId(), reservedSeats);
    }

}
