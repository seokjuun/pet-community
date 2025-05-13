package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<CommonCode, String> {

    // 회원가입 시 역할 설정
    CommonCode findByCodeId(String codeId);
}
