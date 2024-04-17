package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingOutDto createBookingOutDtoResponse(@RequestBody @Valid BookingDto bookingDto,
                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("пришел POST запрос /bookings с userId: {} и bookingDto: {}", userId, bookingDto);
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingOutDto setBookingOutDtoApprovalResponse(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam @NotNull Boolean approved,
                                         @PathVariable Long bookingId) {
        log.info("пришел PATCH запрос /booking/{bookingId} с userId: {} и approved: {} и bookingId: {}", userId, approved, bookingId);
        return bookingService.setBookingApproval(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto findBookingOutDtoByIdResponse(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("пришел GET запрос /booking/{bookingId} с userId: {} и bookingId: {}", userId, bookingId);
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutDto> findBookingsOutDtoOfUserResponse(@RequestParam(defaultValue = "ALL", required = false) String state,
                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        BookingState bookingState = BookingState.valueOf(state);
        log.info("пришел GET запрос /bookings?state с userId: {} и state: {}", userId, state);
        return bookingService.findBookingsOfUser(bookingState, userId);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> findBookingsOutDtoOfOwnerResponse(@RequestParam(defaultValue = "ALL", required = false) String state,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("пришел GET запрос /bookings/owner?state с userId: {} и state: {}", userId, state);
        BookingState bookingState = BookingState.valueOf(state);
        return bookingService.findBookingsOfOwner(bookingState, userId);
    }
}
