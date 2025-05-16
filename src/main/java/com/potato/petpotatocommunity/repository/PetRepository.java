package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.dto.friend.PetResultDto;
import com.potato.petpotatocommunity.entity.Pet;
import com.potato.petpotatocommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    // 일정 - pet 불러오기
    @Query("SELECT new com.potato.petpotatocommunity.dto.friend.PetResultDto(p.user.userId, p.petId, p.name, p.breed.codeName, p.birthday, p.petImage) " +
            "FROM Pet p WHERE p.user.userId IN :friendIds")
    List<PetResultDto> findPetByUserId(@Param("friendIds") List<Long> friendIds);

    @Query("SELECT new com.potato.petpotatocommunity.dto.friend.PetResultDto(p.user.userId, p.petId, p.name, p.breed.codeName, p.birthday, p.petImage) " +
            "FROM Pet p WHERE p.user.userId = :userId")
    List<PetResultDto> findPetByUserId(@Param("userId") Long userId);

    // Pet 등록
    Optional<Pet> findByUser(User user);

    @Query("SELECT p FROM Pet p JOIN FETCH p.breed WHERE p.user = :user")
    List<Pet> findByUserWithBreed(@Param("user") User user);

    List<Pet> findAllByUser(User user);
    Optional<Pet> findByPetIdAndUser(Long petId, User user);

}
