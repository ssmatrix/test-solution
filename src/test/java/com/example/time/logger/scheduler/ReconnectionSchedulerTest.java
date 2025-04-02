package com.example.time.logger.scheduler;

import com.example.time.logger.checker.ConnectionChecker;
import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.repository.TimeRecordRepository;
import com.example.time.logger.util.Buffer;
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
class ReconnectionSchedulerTest {

    @Mock
    private TimeRecordRepository timeRecordRepository;

    @Mock
    private ConnectionChecker connectionChecker;

    @InjectMocks
    private ReconnectionScheduler reconnectionScheduler;

    @Test
    @Description("check that the data from the buffer is saved when the connection is available.")
    void reconnectAndSaveBufferedDataWhenConnectionIsAvailable() {
        when(connectionChecker.isConnection()).thenReturn(true);

        var expectedDateTime = LocalDateTime.now();
        Buffer.addToBuffer(expectedDateTime);

        reconnectionScheduler.reconnectAndSaveBufferedData();

        var captor = ArgumentCaptor.forClass(TimeRecord.class);
        verify(timeRecordRepository, times(1)).save(captor.capture());

        assertEquals(expectedDateTime, captor.getValue().getDateTime());
    }

    @Test
    @Description("check that data from the buffer is not saved when the connection is unavailable.")
    void reconnectAndSaveBufferedDataWhenConnectionIsNotAvailable() {
        when(connectionChecker.isConnection()).thenReturn(false);

        reconnectionScheduler.reconnectAndSaveBufferedData();

        verify(timeRecordRepository, never()).save(any(TimeRecord.class));
    }
}