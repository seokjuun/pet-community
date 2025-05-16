package com.potato.petpotatocommunity.repository;

import com.potato.petpotatocommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 기본 CRUD 자동

    // 로그인 시 사용자 이메일 존재 여부 확인
    Optional<User> findByEmail(String email);

    //    Optional<User> findByUsername(String username);
}
