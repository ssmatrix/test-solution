package com.example.time.logger.scheduler;

import com.example.time.logger.checker.ConnectionChecker;
import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.repository.TimeRecordRepository;
import com.example.time.logger.util.Buffer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeScheduler {

    private final TimeRecordRepository timeRecordRepository;
    private final ConnectionChecker connectionChecker;

    @Scheduled(fixedRate = 1000)
    public void saveCurrentTime() {
        var currentTime = LocalDateTime.now();
        if (connectionChecker.isConnection()) {
            log.info("The DB is working, data is added to the DB: " + timeRecordRepository.save(new TimeRecord(currentTime)));
        } else {
            log.info("Data is added to the buffer: " + currentTime);
            Buffer.addToBuffer(currentTime);
        }
    }
}