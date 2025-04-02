package com.example.time.logger.scheduler;

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

    @InjectMocks
    private TimeScheduler timeScheduler;

    @Test
    @Description("Check that the current time is preserved when a connection is available.")
    void saveCurrentTimeWhenConnectionIsAvailable() {
        when(connectionChecker.isConnection()).thenReturn(true);

        timeScheduler.saveCurrentTime();

        var captor = ArgumentCaptor.forClass(TimeRecord.class);
        verify(timeRecordRepository, times(1)).save(captor.capture());

        var nowTime = LocalDateTime.now();
        assertEquals(nowTime.getMinute(), captor.getValue().getDateTime().getMinute());
    }

    @Test
    @Description("check that the current time is not saved when the connection is unavailable.")
    void saveCurrentTimeWhenConnectionIsNotAvailable() {
        when(connectionChecker.isConnection()).thenReturn(false);

        timeScheduler.saveCurrentTime();

        verify(timeRecordRepository, never()).save(any(TimeRecord.class));
    }
}