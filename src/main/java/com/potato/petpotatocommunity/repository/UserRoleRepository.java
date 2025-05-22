package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Code;
import com.potato.petpotatocommunity.entity.CommonCode;
import com.potato.petpotatocommunity.entity.key.CodeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<CommonCode, String> {
    CommonCode findByCodeId(String codeId);
}
