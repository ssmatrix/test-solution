package com.example.time.logger.controller;

import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.service.TimeRecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/records")
@AllArgsConstructor
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TimeRecord> getAllRecords() {
        return timeRecordService.getAllRecords();
    }
}