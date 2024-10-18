package com.dev.core.log;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/19 00/02
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs")
public class LogController {
    private final LogRepository logRepository;
    @GetMapping
    public List<Log> getLogs(){
        return logRepository.findAll();
    }
}
