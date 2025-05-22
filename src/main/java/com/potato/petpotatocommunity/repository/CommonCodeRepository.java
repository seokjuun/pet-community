package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Code;
import com.potato.petpotatocommunity.entity.key.CodeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonCodeRepository extends JpaRepository<Code, CodeKey> {
}