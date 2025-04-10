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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReconnectionSchedulerTest {

    @Mock
    private TimeRecordRepository timeRecordRepository;

    @Mock
    private ConnectionChecker connectionChecker;

    @Mock
    private Buffer buffer;

    @InjectMocks
    private ReconnectionScheduler reconnectionScheduler;

    @Test
    @Description("Check that data from buffer is saved when connection is available.")
    void reconnectAndSaveBufferedDataWhenConnectionIsAvailable() {
        when(connectionChecker.isConnection()).thenReturn(true);

        LocalDateTime expectedDateTime = LocalDateTime.now();

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            when(buffer.isEmpty()).thenReturn(false, true);
            when(buffer.pollFromBuffer()).thenReturn(expectedDateTime);
            runnable.run();
            return null;
        }).when(buffer).executeInLock(any(Runnable.class));

        reconnectionScheduler.reconnectAndSaveBufferedData();

        ArgumentCaptor<TimeRecord> captor = ArgumentCaptor.forClass(TimeRecord.class);
        verify(timeRecordRepository, times(1)).save(captor.capture());
        assertEquals(expectedDateTime, captor.getValue().getDateTime());

        verify(buffer).setRecoveryInProgress(true);
        verify(buffer).setRecoveryInProgress(false);
    }

    @Test
    @Description("Check that data from buffer is not saved when connection is unavailable.")
    void reconnectAndSaveBufferedDataWhenConnectionIsNotAvailable() {
        when(connectionChecker.isConnection()).thenReturn(false);

        reconnectionScheduler.reconnectAndSaveBufferedData();

        verify(buffer, never()).executeInLock(any());
        verify(timeRecordRepository, never()).save(any());
    }
}