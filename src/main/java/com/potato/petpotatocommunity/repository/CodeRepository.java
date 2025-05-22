package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Code;
import com.potato.petpotatocommunity.entity.key.CodeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, CodeKey> {
}
