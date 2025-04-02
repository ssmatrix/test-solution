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
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconnectionScheduler {

    private final TimeRecordRepository timeRecordRepository;
    private final ConnectionChecker connectionChecker;

    @Scheduled(fixedRate = 5000)
    public void reconnectAndSaveBufferedData() {
        try {
            if (connectionChecker.isConnection()) {
                while (!Buffer.isEmpty()) {
                    Optional<LocalDateTime> timestamp = Optional.ofNullable(Buffer.pollFromBuffer());
                    timestamp.ifPresent(dateTime -> {
                        log.info("Data from the buffer is added to the DB: " + dateTime);
                        timeRecordRepository.save(new TimeRecord(dateTime));
                    });
                }
            }
        } catch (Exception e) {
            log.error("Something happened to the buffer: " + e.getMessage());
        }
    }
}