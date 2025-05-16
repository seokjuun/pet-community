package com.potato.petpotatocommunity.dto.schedules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulesResultDto {
    private Long scheduleId;
    private int dateOfWeek;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String timeSlot;
    private String location;
    private String petName;
}
