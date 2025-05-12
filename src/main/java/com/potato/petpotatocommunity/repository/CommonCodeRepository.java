package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
//    Optional<CommonCode> findByCodeName(String codeName);
}