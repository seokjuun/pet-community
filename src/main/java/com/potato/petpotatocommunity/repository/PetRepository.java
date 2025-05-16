package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.dto.friend.PetResultDto;
import com.potato.petpotatocommunity.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT new com.potato.petpotatocommunity.dto.friend.PetResultDto(p.user.userId, p.petId, p.name, p.breed.codeName, p.birthday, p.petImage) " +
            "FROM Pet p WHERE p.user.userId IN :friendIds")
    List<PetResultDto> findPetByUserId(@Param("friendIds") List<Long> friendIds);

    @Query("SELECT new com.potato.petpotatocommunity.dto.friend.PetResultDto(p.user.userId, p.petId, p.name, p.breed.codeName, p.birthday, p.petImage) " +
            "FROM Pet p WHERE p.user.userId = :userId")
    List<PetResultDto> findPetByUserId(@Param("userId") Long userId);
}
