package com.dev.core.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/18 21/55
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
    Optional<Project> findByName(String name);
}
