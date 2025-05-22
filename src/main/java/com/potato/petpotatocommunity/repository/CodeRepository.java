package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.Code;
import com.potato.petpotatocommunity.entity.key.CodeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, CodeKey> {
    // group_code가 특정 값인 코드 목록 조회
    @Query("SELECT c FROM Code c WHERE c.codeKey.groupCode = :groupCode")
    List<Code> findByGroupCode(@Param("groupCode") String groupCode);

}
