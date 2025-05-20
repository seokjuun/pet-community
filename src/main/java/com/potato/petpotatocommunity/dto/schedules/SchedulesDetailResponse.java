package com.potato.petpotatocommunity.dto.schedules;

import com.potato.petpotatocommunity.dto.friend.PetResultDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesDetailResponse {
    private PetResultDto pet;
    private Integer dayOfWeek;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String timeSlot;
    private String location;
    private String notes;
}
