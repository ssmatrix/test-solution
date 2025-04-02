package com.example.time.logger.model.dto;

public record ErrorDto(
        String errorMessage,
        Integer errorCode) {
}