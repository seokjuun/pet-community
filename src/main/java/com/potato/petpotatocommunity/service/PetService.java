package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.pet.PetDto;
import com.potato.petpotatocommunity.dto.pet.PetUpdateDto;

import java.util.List;

public interface PetService {
    List<PetDto> getMyPet(Long userId);

    PetDto createMyPet(Long userId, PetUpdateDto dto);


    void updateMyPet(Long userId, Long petId, PetUpdateDto dto);

}