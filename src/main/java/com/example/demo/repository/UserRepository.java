package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { 

    // 通过用户名查询用户 (假设用户名唯一)
    Optional<User> findByName(String name);

    // 通过用户名查询所有用户 (如果用户名可能不唯一)
    List<User> findAllByName(String name);
}
