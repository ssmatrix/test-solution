package com.example.time.logger.service;

import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.repository.TimeRecordRepository;
import com.example.time.logger.service.impl.TimeRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceTest {

    @Mock
    private TimeRecordRepository timeRecordRepository;

    @InjectMocks
    private TimeRecordServiceImpl timeRecordService;

    @Test
    @Description("checking that the getAllRecords method returns the expected list of records.")
    void getAllRecordsShouldReturnExpectedList() {
        List<TimeRecord> recordList = List.of(
                new TimeRecord(LocalDateTime.now()),
                new TimeRecord(LocalDateTime.now())
        );

        when(timeRecordRepository.findAll()).thenReturn(recordList);

        var actual = timeRecordService.getAllRecords();

        assertEquals(2, actual.size());
        assertEquals(recordList, actual);
    }

    @Test
    @Description("check that the getAllRecords method returns an empty list when there are no records in the repository.")
    void getAllRecordsShouldReturnEmptyList() {
        when(timeRecordRepository.findAll()).thenReturn(List.of());

        var actual = timeRecordService.getAllRecords();

        assertEquals(0, actual.size());
    }
}