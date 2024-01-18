package com.miml.c2k.domain.seat.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.miml.c2k.domain.member.Member;
import com.miml.c2k.domain.member.OAuthProvider;
import com.miml.c2k.domain.member.repository.MemberRepository;
import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import com.miml.c2k.domain.schedule.dto.ScheduleSavingDto;
import com.miml.c2k.domain.schedule.service.ScheduleService;
import com.miml.c2k.domain.screen.Screen;
import com.miml.c2k.domain.screen.repository.ScreenRepository;
import com.miml.c2k.domain.seat.Seat.SeatNameType;
import com.miml.c2k.domain.seat.dto.SeatRequestDto;
import com.miml.c2k.domain.seat.dto.SeatResponseDto;
import com.miml.c2k.domain.theater.Theater;
import com.miml.c2k.domain.theater.repository.TheaterRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SeatServiceIntegrationTest {

    @Autowired
    private SeatService seatService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("동시에 같은 좌석을 예매할 수 없다.")
    void success_to_not_reserve_same_seat_concurrently() throws Exception{
        // given
        Member leeYoungJi = memberRepository.save(
            Member.builder().nickname("Lee Young Ji").email("lee@naver.com")
                .oAuthProvider(OAuthProvider.KAKAO).build());
        Member codeKunst = memberRepository.save(
            Member.builder().nickname("Code KUNST").email("cokun@naver.com")
                .oAuthProvider(OAuthProvider.KAKAO).build());
        Movie movie = movieRepository.save(
            Movie.builder().title("Go High").code("1004").audienceCount(100L).build());
        Theater theater = theaterRepository.save(Theater.builder().name("High School Rapper").build());
        Screen screen = screenRepository.save(Screen.builder().num(3).seatCount(10).theater(theater).build());

        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        scheduleService.saveSchedule(
            new ScheduleSavingDto(movie.getId(), screen.getId(), nowLocalDateTime,
                nowLocalDateTime.plusHours(1)));

        Long testScheduleId = scheduleService.getSchedulesBy(movie.getId(), theater.getId(),
            nowLocalDateTime.toLocalDate()).get(0).getId();
        SeatRequestDto seatRequestDto1 = SeatRequestDto.builder()
            .seatNameTypes(List.of(SeatNameType.J10, SeatNameType.J11))
            .scheduleId(testScheduleId).build();
        SeatRequestDto seatRequestDto2 = SeatRequestDto.builder()
            .seatNameTypes(List.of(SeatNameType.J11, SeatNameType.J12))
            .scheduleId(testScheduleId).build();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        // when
        executorService.execute(() -> {
            try {
                seatService.reserveSeat(seatRequestDto1, leeYoungJi.getId());
            } catch (Exception e) {
                log.info("lee young ji encountered exception: {}", e.getMessage());
            }
            finally {
                countDownLatch.countDown();
            }
        });
        executorService.execute(() -> {
            try {
                seatService.reserveSeat(seatRequestDto2, codeKunst.getId());
            } catch (Exception e) {
                log.info("code KUNST encountered exception: {}", e.getMessage());
            }
            finally {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();

        // then
        List<SeatResponseDto> allSeats = seatService.getAllSeats(testScheduleId);
        int reservedSeatCount = allSeats.stream()
            .mapToInt(seat -> seat.isReserved() ? 1 : 0).sum();

        assertThat(reservedSeatCount).isEqualTo(2);
    }
}
