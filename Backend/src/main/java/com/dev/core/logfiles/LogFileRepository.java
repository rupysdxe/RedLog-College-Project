package com.dev.core.logfiles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/18 23/18
 */
@Repository
public interface LogFileRepository extends JpaRepository<LogFile,Integer> {
    Optional<LogFile> findByName(String name);
}
