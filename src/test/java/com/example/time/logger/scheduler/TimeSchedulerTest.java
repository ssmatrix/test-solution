package com.example.time.logger.scheduler;

import com.example.time.logger.buffer.Buffer;
import com.example.time.logger.checker.ConnectionChecker;
import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.repository.TimeRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeSchedulerTest {

    @Mock
    private TimeRecordRepository timeRecordRepository;

    @Mock
    private ConnectionChecker connectionChecker;

    @Mock
    private Buffer buffer;

    @InjectMocks
    private TimeScheduler timeScheduler;

    @BeforeEach
    void setUp() {
    }

    @Test
    @Description("Should save data to DB when connection is available and recovery is not in progress.")
    void generateCurrentTimeShouldSaveDataToDatabaseWhenConnectionIsAvailable() throws InterruptedException {
        when(buffer.isRecoveryInProgress()).thenReturn(false);
        when(connectionChecker.isConnection()).thenReturn(true);

        timeScheduler.generateCurrentTime();
        Thread.sleep(200); // дать время executor'у

        verify(timeRecordRepository, timeout(500)).save(any(TimeRecord.class));
        verify(buffer, never()).addToBuffer(any(LocalDateTime.class));
    }

    @Test
    @Description("Should add data to buffer when connection is unavailable.")
    void generateCurrentTimeAddToBufferWhenConnectionIsNotAvailable() throws InterruptedException {
        when(buffer.isRecoveryInProgress()).thenReturn(false);
        when(connectionChecker.isConnection()).thenReturn(false);

        timeScheduler.generateCurrentTime();
        Thread.sleep(200);

        verify(buffer, timeout(500)).addToBuffer(any(LocalDateTime.class));
        verify(timeRecordRepository, never()).save(any(TimeRecord.class));
    }

    @Test
    @Description("Should add to buffer if recovery is in progress regardless of connection.")
    void generateCurrentTimeAddToBufferWhenRecoveryInProgress() throws InterruptedException {
        when(buffer.isRecoveryInProgress()).thenReturn(true);

        timeScheduler.generateCurrentTime();
        Thread.sleep(200);

        verify(buffer, timeout(500)).addToBuffer(any(LocalDateTime.class));
        verify(timeRecordRepository, never()).save(any(TimeRecord.class));
    }
}