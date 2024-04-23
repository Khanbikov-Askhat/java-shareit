package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingViewDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long bookerId;
    private Long itemId;
}
