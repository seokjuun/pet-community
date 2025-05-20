package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.friend.PetResultDto;
import com.potato.petpotatocommunity.dto.schedules.SchedulesDetailResponse;
import com.potato.petpotatocommunity.dto.schedules.SchedulesInsertRequest;
import com.potato.petpotatocommunity.dto.schedules.SchedulesResultDto;
import com.potato.petpotatocommunity.repository.PetRepository;

import java.util.List;

public interface SchedulesService {
    List<SchedulesResultDto> getSchedules(Long userId);
    List<PetResultDto> getPets(Long userId);
    String insertSchedule(SchedulesInsertRequest request, Long userId);
    SchedulesDetailResponse getScheduleDetail(Long scheduleId);
    String deleteSchedule(Long scheduleId, Long userId);
}
