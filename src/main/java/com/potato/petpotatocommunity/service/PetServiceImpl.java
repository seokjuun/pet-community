package com.potato.petpotatocommunity.service;

import com.potato.petpotatocommunity.dto.pet.PetDto;
import com.potato.petpotatocommunity.dto.pet.PetUpdateDto;
import com.potato.petpotatocommunity.entity.CommonCode;
import com.potato.petpotatocommunity.entity.Pet;
import com.potato.petpotatocommunity.entity.User;
import com.potato.petpotatocommunity.exception.NotFoundException;
import com.potato.petpotatocommunity.repository.ExCommonCodeRepository;
import com.potato.petpotatocommunity.repository.PetRepository;
import com.potato.petpotatocommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final ExCommonCodeRepository exCommonCodeRepository;

    @Override
    public List<PetDto> getMyPet(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        List<Pet> pets = petRepository.findByUserWithBreed(user);


        return pets.stream().map(pet -> PetDto.builder()
                .petId(pet.getPetId())
                .name(pet.getName())
                .breedName(pet.getBreed().getCodeName())
                .birthday(pet.getBirthday() != null ? pet.getBirthday().toString() : null)
                .info(pet.getInfo())
                .petImage(pet.getPetImage())
                .build()).toList();
    }



    @Override
    public PetDto createMyPet(Long userId, PetUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        CommonCode breed = exCommonCodeRepository.findById(dto.getBreedId())
                .orElseThrow(() -> new NotFoundException("해당 품종 코드가 존재하지 않습니다."));

        Pet newPet = Pet.builder()
                .user(user)
                .breed(breed)
                .name(dto.getName())
                .birthday(dto.getBirthday() != null ? LocalDate.parse(dto.getBirthday()) : null)
                .info(dto.getInfo())
                .petImage(dto.getPetImage())
                .build();

        Pet saved = petRepository.save(newPet);

        return PetDto.builder()
                .petId(saved.getPetId())
                .name(saved.getName())
                .breedName(saved.getBreed().getCodeName())
                .birthday(saved.getBirthday() != null ? saved.getBirthday().toString() : null)
                .info(saved.getInfo())
                .petImage(saved.getPetImage())
                .build();
    }



    @Override
    public void updateMyPet(Long userId, Long petId, PetUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Pet pet = petRepository.findByPetIdAndUser(petId, user)
                .orElseThrow(() -> new NotFoundException("해당 반려동물을 찾을 수 없습니다."));

        CommonCode breed = exCommonCodeRepository.findById(dto.getBreedId())
                .orElseThrow(() -> new NotFoundException("해당 품종 코드가 존재하지 않습니다."));

        pet.setName(dto.getName());
        pet.setBreed(breed);
        pet.setBirthday(dto.getBirthday() != null ? LocalDate.parse(dto.getBirthday()) : null);
        pet.setInfo(dto.getInfo());
        pet.setPetImage(dto.getPetImage());

        petRepository.save(pet);
    }

}
