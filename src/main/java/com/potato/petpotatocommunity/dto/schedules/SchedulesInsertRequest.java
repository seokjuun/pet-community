package com.potato.petpotatocommunity.dto.schedules;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesInsertRequest {
    private Long petId;
    private Integer dayOfWeek;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String timeSlot;
    private String location;
    private String notes;
}
