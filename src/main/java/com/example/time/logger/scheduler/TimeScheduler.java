package com.example.time.logger.scheduler;

import com.example.time.logger.buffer.Buffer;
import com.example.time.logger.checker.ConnectionChecker;
import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.repository.TimeRecordRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeScheduler {

    private final TimeRecordRepository timeRecordRepository;
    private final ConnectionChecker connectionChecker;
    private final Buffer buffer;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Scheduled(fixedRate = 1000)
    public void generateCurrentTime() {
        var currentTime = LocalDateTime.now();
        executorService.submit(() -> processCurrentTime(currentTime));
    }

    private void processCurrentTime(LocalDateTime currentTime) {
        if (connectionChecker.isConnection()) {
            log.info("The DB is working, data is added to the DB: " + timeRecordRepository.save(new TimeRecord(currentTime)));
        } else {
            log.info("Data is added to the buffer: " + currentTime);
            buffer.addToBuffer(currentTime);
        }
    }

    @PreDestroy
    private void shutdown() {
        executorService.shutdown();
    }
}