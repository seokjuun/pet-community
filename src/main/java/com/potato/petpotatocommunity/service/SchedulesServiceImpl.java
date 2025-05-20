package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.friend.PetResultDto;
import com.potato.petpotatocommunity.dto.schedules.SchedulesDetailResponse;
import com.potato.petpotatocommunity.dto.schedules.SchedulesInsertRequest;
import com.potato.petpotatocommunity.dto.schedules.SchedulesResultDto;
import com.potato.petpotatocommunity.entity.Pet;
import com.potato.petpotatocommunity.entity.User;
import com.potato.petpotatocommunity.entity.WalkSchedule;
import com.potato.petpotatocommunity.exception.PetException;
import com.potato.petpotatocommunity.exception.ScheduleException;
import com.potato.petpotatocommunity.exception.UserException;
import com.potato.petpotatocommunity.repository.PetRepository;
import com.potato.petpotatocommunity.repository.SchedulesRepository;
import com.potato.petpotatocommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchedulesServiceImpl implements SchedulesService {
    private final SchedulesRepository schedulesRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;


    @Override
    public List<SchedulesResultDto> getSchedules(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()){
            throw new UserException.UserNotFoundException(userId);
        }

        User user = optionalUser.get();

        return schedulesRepository.findSchedulesByUserId(user.getUserId());
    }

    @Transactional
    @Override
    public SchedulesDetailResponse getScheduleDetail(Long scheduleId) {
        Optional<WalkSchedule> optSchedule = schedulesRepository.findById(scheduleId);

        if (optSchedule.isEmpty()) {
            throw new ScheduleException.ScheduleNotFoundException(scheduleId);
        }

        WalkSchedule walkSchedule = optSchedule.get();

        PetResultDto petResultDto = PetResultDto.builder()
                .id(walkSchedule.getPet().getPetId())
                .name(walkSchedule.getPet().getName())
                .breed(walkSchedule.getPet().getBreed().getCodeName())
                .birthday(walkSchedule.getPet().getBirthday())
                .imageUrl(walkSchedule.getPet().getPetImage())
                .build();

        SchedulesDetailResponse schedulesDetailResponse = SchedulesDetailResponse.builder()
                .pet(petResultDto)
                .startTime(walkSchedule.getStartTime())
                .endTime(walkSchedule.getEndTime())
                .timeSlot(walkSchedule.getTimeSlot())
                .dayOfWeek(walkSchedule.getDayOfWeek())
                .location(walkSchedule.getLocation())
                .notes(walkSchedule.getNotes())
                .build();


        return schedulesDetailResponse;
    }

    @Override
    public List<PetResultDto> getPets(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserException.UserNotFoundException(userId);
        }
        User user = optionalUser.get();

        return petRepository.findPetByUserId(user.getUserId());
    }

    @Override
    public String insertSchedule(SchedulesInsertRequest request, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserException.UserNotFoundException(userId);
        }
        User user = optionalUser.get();

        Optional<Pet> optionalPet = petRepository.findById(request.getPetId());
        if(optionalPet.isEmpty()){
            throw new PetException.PetNotFoundException(request.getPetId());
        }
        Pet pet = optionalPet.get();

        // 해당 반려동물이 사용자의 것인지 확인
        if (!pet.getUser().getUserId().equals(userId)) {
            throw new ScheduleException.PetNotOwnedException(userId, pet.getPetId());
        }

//        List<WalkSchedule> existingSchedules = schedulesRepository.findByUserIdAndDayOfWeekAndTimeSlot(
//                userId, request.getDayOfWeek(), request.getTimeSlot());
//
//        if (!existingSchedules.isEmpty()) {
//            throw new ScheduleException.ScheduleOverlapException(request.getDayOfWeek(), request.getTimeSlot());
//        }

        WalkSchedule schedules = WalkSchedule.builder()
                .user(user)
                .pet(pet)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .timeSlot(request.getTimeSlot())
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();

        schedulesRepository.save(schedules);

        return "success";
    }

    public String deleteSchedule(Long scheduleId, Long userId) {
        Optional<WalkSchedule> optionalSchedule = schedulesRepository.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            throw new ScheduleException.ScheduleNotFoundException(scheduleId);
        }

        WalkSchedule schedule = optionalSchedule.get();

        if (!schedule.getUser().getUserId().equals(userId)) {
            throw new ScheduleException.UnauthorizedScheduleAccessException(userId, scheduleId);
        }

        schedulesRepository.delete(schedule);

        return "success";
    }

}
