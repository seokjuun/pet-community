package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.dto.schedules.SchedulesResultDto;
import com.potato.petpotatocommunity.entity.WalkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchedulesRepository extends JpaRepository<WalkSchedule, Long> {
    @Query("SELECT new com.potato.petpotatocommunity.dto.schedules.SchedulesResultDto(" +
            "s.scheduleId, s.dayOfWeek, s.startTime, s.endTime, s.timeSlot, s.location, p.name) " +
            "FROM WalkSchedule s " +
            "JOIN s.pet p " +
            "WHERE s.user.userId = :userId")
    List<SchedulesResultDto> findSchedulesByUserId(@Param("userId") Long userId);

}
