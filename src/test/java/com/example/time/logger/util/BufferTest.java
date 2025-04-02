package com.example.time.logger.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BufferTest {

    @BeforeEach
    public void setUp() {
        while (!Buffer.isEmpty()) {
            Buffer.pollFromBuffer();
        }
    }

    @Test
    @Description("check that the addToBuffer method adds a new element to the buffer.")
    void addToBufferShouldAddNewElement() {
        var dateTime = LocalDateTime.now();
        Buffer.addToBuffer(dateTime);

        assertFalse(Buffer.isEmpty());
        assertEquals(dateTime, Buffer.pollFromBuffer());
    }

    @Test
    @Description("check that the isEmpty method returns true when the buffer is empty.")
    void isEmptyWhenBufferIsEmpty() {
        assertTrue(Buffer.isEmpty());
    }

    @Test
    @Description("check that the isEmpty method returns false when the buffer is not empty.")
    void isEmptyWhenBufferNotEmpty() {
        Buffer.addToBuffer(LocalDateTime.now());

        assertFalse(Buffer.isEmpty());
    }

    @Test
    @Description("check that the pollFromBuffer method returns the last element added.")
    void pollFromBufferShouldReturnLastElement() {
        var dateTime = LocalDateTime.now();
        Buffer.addToBuffer(dateTime);

        var polledDateTime = Buffer.pollFromBuffer();

        assertEquals(dateTime, polledDateTime);
    }

    @Test
    @Description("check that the pollFromBuffer method returns null when the buffer is empty.")
    void pollFromBufferWhenBufferIsEmpty() {
        assertNull(Buffer.pollFromBuffer());
    }
}