package com.example.time.logger.scheduler;

import com.example.time.logger.buffer.Buffer;
import com.example.time.logger.checker.ConnectionChecker;
import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.repository.TimeRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconnectionScheduler {

    private final TimeRecordRepository timeRecordRepository;
    private final ConnectionChecker connectionChecker;
    private final Buffer buffer;

    @Scheduled(fixedRate = 5000)
    public void reconnectAndSaveBufferedData() {
        if (connectionChecker.isConnection()) {
            buffer.setRecoveryInProgress(true);

            buffer.executeInLock(() -> {
                try {
                    while (!buffer.isEmpty()) {
                        Optional.ofNullable(buffer.pollFromBuffer())
                                .ifPresent(dateTime -> {
                                    log.info("Restoring from buffer to DB: " + dateTime);
                                    timeRecordRepository.save(new TimeRecord(dateTime));
                                });
                    }
                } catch (Exception e) {
                    log.error("Error restoring buffer: " + e.getMessage(), e);
                } finally {
                    buffer.setRecoveryInProgress(false);
                }
            });
        }
    }
}
