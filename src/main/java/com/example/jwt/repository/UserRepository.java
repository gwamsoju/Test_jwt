package com.example.jwt.repository;

import com.example.jwt.vo.User2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User2, Long> {
    public User2 findByUsername(String username);
}
