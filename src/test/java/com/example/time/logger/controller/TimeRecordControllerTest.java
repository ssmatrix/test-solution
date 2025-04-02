package com.example.time.logger.controller;

import com.example.time.logger.config.PostgresSqlContainerInitializer;
import com.example.time.logger.model.entity.TimeRecord;
import com.example.time.logger.service.TimeRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TimeRecordControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TimeRecordService timeRecordService;

    @Test
    @Description("checking that the getAllRecords method returns the expected list of records.")
    void getAllRecordsShouldReturnExpectedList() throws Exception {
        List<TimeRecord> recordList = List.of(
                new TimeRecord(LocalDateTime.now()),
                new TimeRecord(LocalDateTime.now())
        );

        when(timeRecordService.getAllRecords()).thenReturn(recordList);

        mockMvc.perform(get("/records")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }
}