package com.example.time.logger.scheduler;

import com.example.time.logger.buffer.Buffer;
import com.example.time.logger.checker.ConnectionChecker;
import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.repository.TimeRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    @Description("Check that the current time is preserved when a connection is available.")
    void generateCurrentTimeShouldSaveDataToDatabaseWhenConnectionIsAvailable() throws InterruptedException {
        when(connectionChecker.isConnection()).thenReturn(true);

        timeScheduler.generateCurrentTime();

        Thread.sleep(100);

        verify(timeRecordRepository).save(any(TimeRecord.class));
        verify(buffer, never()).addToBuffer(any(LocalDateTime.class));
    }

    @Test
    @Description("check that the current time is not saved when the connection is unavailable.")
    void generateCurrentTimeAddToBufferWhenConnectionIsNotAvailable() throws InterruptedException {
        when(connectionChecker.isConnection()).thenReturn(false);

        timeScheduler.generateCurrentTime();

        Thread.sleep(100);

        verify(buffer).addToBuffer(any(LocalDateTime.class));
        verify(timeRecordRepository, never()).save(any(TimeRecord.class));
    }
}