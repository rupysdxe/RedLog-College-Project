package com.dev.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 20/33
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findUserByUsername(String username);
}
