package com.daou.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daou.deploy.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginId(String loginId);

    User findByEmail(String email);
}
