package com.example.time.logger.service.impl;

import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.repository.TimeRecordRepository;
import com.example.time.logger.service.TimeRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TimeRecordServiceImpl implements TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TimeRecord> getAllRecords() {
        return timeRecordRepository.findAll();
    }
}