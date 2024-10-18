package com.dev.core.log;

import com.dev.core.application.Project;
import com.dev.core.logfiles.LogFile;
import com.dev.core.logfiles.LogFileRepository;
import com.dev.core.logfiles.LogFileStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/17 12/58
 */
@Service
@Slf4j
@Transactional
public abstract class AbstractLogFileImporter {
    @Autowired
    protected LogRepository logRepository;
    @Autowired
    protected LogFileRepository logFileRepository;
    // Method to be implemented by subclass to return the relevant project
    protected abstract Project getProject();
    protected abstract Log mapLog(String record);
    // Method to return a list of files to import
    protected List<File> getFilesToImport() {
        ArrayList<File> files = new ArrayList<>();
        File importFolder = new File(getProject().getLogImportFolder());

        if (importFolder.exists() && importFolder.isDirectory()) {
            String logFileExtension = getProject().getLogFileExtension();
            File[] fileArray = importFolder.listFiles();
            if (fileArray != null) {
                for (File file : fileArray) {
                    if (file.isFile()) {
                        if (logFileExtension == null || file.getName().endsWith(logFileExtension)) {
                            Optional<LogFile> logFile = logFileRepository.findByName(file.getName());
                            if(!logFile.isPresent()){
                                files.add(file);
                            }
                        }
                    }
                }
            }
        } else {
            if (importFolder.mkdirs()) {
                System.out.println("Log import folder created: " + importFolder.getPath());
            } else {
                System.err.println("Failed to create log import folder: " + importFolder.getPath());
                return files;
            }
        }

        return files;
    }

    // Method to process a file and return its content as a List of Strings (one per line)
    protected List<String> processFile(File file) {
        List<String> lines = new ArrayList<>();
        LogFile logFile = new LogFile();
        logFile.setName(file.getName());
        logFile.setStatus(LogFileStatus.PROCESSING);
        logFile.setTimestamp(LocalDateTime.now());
        logFileRepository.save(logFile);
        // Use BufferedReader to read the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line); // Add each line to the list
            }
        } catch (IOException e) {
            // Handle the IOException (log or throw an exception)
            System.err.println("Error reading file: " + file.getPath());
            e.printStackTrace();
        }

        return lines; // Return the list of lines
    }

    protected void execute(){
        log.info("Running the job {}", this.getClass().getName());
        List<File> logFiles = getFilesToImport();
        for(File file:logFiles){
            List<String> logRecords = processFile(file);
            log.info("Found total records {}",logRecords.size());
            List<Log> logs = new ArrayList<>();
            for(String record:logRecords){
                log.info("Adding log record {}",record);
                Log temp = mapLog(record);
                if(temp!=null)logs.add(temp);
            }
            logRepository.saveAll(logs);
            LogFile logFile = logFileRepository.findByName(file.getName()).get();
            logFile.setStatus(LogFileStatus.SUCCESS);
            logFile.setCompletedDate(LocalDateTime.now());
            logFileRepository.save(logFile);
            log.info("Save all records");
        }

    }

}
