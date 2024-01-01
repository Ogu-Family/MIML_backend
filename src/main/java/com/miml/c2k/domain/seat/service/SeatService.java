package com.miml.c2k.domain.seat.service;

import com.miml.c2k.domain.seat.Seat;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.dto.ReservedSeatResponseDto;
import com.miml.c2k.domain.seat.dto.SeatResponseDto;
import com.miml.c2k.domain.seat.repository.SeatRepository;
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


}
