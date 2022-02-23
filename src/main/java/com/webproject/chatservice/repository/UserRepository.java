package com.webproject.chatservice.repository;

import com.webproject.chatservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByKakaoId(Long kakaoId);

    List<User> findAllByOrderByEmail();
}