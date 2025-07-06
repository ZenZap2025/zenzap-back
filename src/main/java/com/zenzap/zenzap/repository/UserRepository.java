package com.zenzap.zenzap.repository;

import com.zenzap.zenzap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmailAddress(String email);
    Optional<User> findByResetToken(String resetToken);
}

