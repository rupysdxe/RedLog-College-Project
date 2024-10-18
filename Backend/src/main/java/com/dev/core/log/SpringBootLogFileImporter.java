package com.dev.core.log;

import com.dev.core.application.Project;
import com.dev.core.application.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/18 21/54
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SpringBootLogFileImporter extends AbstractLogFileImporter{
    private final ProjectRepository projectRepository;
    @Override
    protected Project getProject() {
        Optional<Project> project= projectRepository.findByName("spring.boot");
        if(!project.isPresent()){
            throw new RuntimeException("Project Not found");
        }
        return project.get();
    }
    @Override
    protected Log mapLog(String record) {
        Log log = new Log();
        // Split the record into key-value pairs
        String[] parts = record.split(",");

        try {
            // Initialize variables to store values
            String dateString = null;
            String level = null;
            String message = null;

            // Iterate over each part to extract key-value pairs
            for (String part : parts) {
                String[] keyValue = part.split("=", 2); // Split on the first '=' only
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    // Assign values based on the key
                    switch (key) {
                        case "date":
                            dateString = value;
                            break;
                        case "level":
                            level = value;
                            break;
                        case "message":
                            message = value;
                            break;
                    }
                }
            }

            // Set the fields of the Log object
            if (dateString != null) {
                log.setTimestamp(LocalDateTime.parse(dateString.replace(" ", "T")));
            }
            if (level != null) {
                log.setLevel(level);
            }
            if (message != null) {
                log.setMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

        return log;
    }


    @Scheduled(fixedDelay = 100,timeUnit = TimeUnit.SECONDS)
    protected void run() {
        execute();
    }

}
