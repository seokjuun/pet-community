package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.friend.PetResultDto;
import com.potato.petpotatocommunity.dto.schedules.SchedulesDetailResponse;
import com.potato.petpotatocommunity.dto.schedules.SchedulesInsertRequest;
import com.potato.petpotatocommunity.dto.schedules.SchedulesResultDto;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.service.SchedulesService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class SchedulesController {
    private final SchedulesService schedulesService;

    @GetMapping
    public ResponseEntity<List<SchedulesResultDto>> getSchedules(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Long userId = user.getUserId();
        List<SchedulesResultDto> schedules = schedulesService.getSchedules(userId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{friendId}")
    public ResponseEntity<List<SchedulesResultDto>> getFriendSchedules(@PathVariable("friendId") Long friendId) {
        List<SchedulesResultDto> schedules = schedulesService.getSchedules(friendId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/pet")
    public ResponseEntity<List<PetResultDto>> getFriendSchedules(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Long userId = user.getUserId();
        List<PetResultDto> pets = schedulesService.getPets(userId);
        return ResponseEntity.ok(pets);
    }

    @PostMapping
    public ResponseEntity<String> insertSchedule(
            @RequestBody SchedulesInsertRequest request, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Long userId = user.getUserId();
        System.out.println(request.getPetId());
        String message = schedulesService.insertSchedule(request, userId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/detail/{scheduleId}")
    public ResponseEntity<SchedulesDetailResponse> getScheduleDetail(@PathVariable("scheduleId") Long scheduleId) {
        SchedulesDetailResponse response = schedulesService.getScheduleDetail(scheduleId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable("scheduleId") Long scheduleId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Long userId = user.getUserId();

        String message = schedulesService.deleteSchedule(scheduleId, userId);
        return ResponseEntity.ok(message);
    }
}
