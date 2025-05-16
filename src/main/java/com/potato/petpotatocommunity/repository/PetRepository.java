package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Pet;
import com.potato.petpotatocommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByUser(User user);

    @Query("SELECT p FROM Pet p JOIN FETCH p.breed WHERE p.user = :user")
    List<Pet> findByUserWithBreed(@Param("user") User user);

    List<Pet> findAllByUser(User user);
    Optional<Pet> findByPetIdAndUser(Long petId, User user);






}
