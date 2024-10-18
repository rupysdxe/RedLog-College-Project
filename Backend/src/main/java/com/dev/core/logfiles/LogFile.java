package com.dev.core.logfiles;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/18 23/15
 */
@Entity
@Data
public class LogFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @Enumerated(value = EnumType.STRING)
    private LogFileStatus status;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    @Column(name = "completedDate")
    private LocalDateTime completedDate;

}
