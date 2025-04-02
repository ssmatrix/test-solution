package com.example.time.logger.service;

import com.example.time.logger.model.entity.TimeRecord;

import java.util.List;

public interface TimeRecordService {

    List<TimeRecord> getAllRecords();
}