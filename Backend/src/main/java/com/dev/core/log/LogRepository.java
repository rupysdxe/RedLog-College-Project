package com.dev.core.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/18 22/23
 */
@Repository
public interface LogRepository extends JpaRepository<Log,Long> {
}
