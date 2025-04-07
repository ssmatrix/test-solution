package com.example.time.logger.buffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BufferConcurrentLinkedQueueImplTest {

    @Autowired
    private Buffer buffer;

    @BeforeEach
    public void setUp() {
        while (!buffer.isEmpty()) {
            buffer.pollFromBuffer();
        }
    }

    @Test
    @Description("check that the addToBuffer method adds a new element to the buffer.")
    void addToBufferShouldAddNewElement() {
        var dateTime = LocalDateTime.now();
        buffer.addToBuffer(dateTime);

        assertFalse(buffer.isEmpty());
        assertEquals(dateTime, buffer.pollFromBuffer());
    }

    @Test
    @Description("check that the isEmpty method returns true when the buffer is empty.")
    void isEmptyWhenBufferIsEmpty() {
        assertTrue(buffer.isEmpty());
    }

    @Test
    @Description("check that the isEmpty method returns false when the buffer is not empty.")
    void isEmptyWhenBufferNotEmpty() {
        buffer.addToBuffer(LocalDateTime.now());

        assertFalse(buffer.isEmpty());
    }

    @Test
    @Description("check that the pollFromBuffer method returns the last element added.")
    void pollFromBufferShouldReturnLastElement() {
        var dateTime = LocalDateTime.now();
        buffer.addToBuffer(dateTime);

        var polledDateTime = buffer.pollFromBuffer();

        assertEquals(dateTime, polledDateTime);
    }

    @Test
    @Description("check that the pollFromBuffer method returns null when the buffer is empty.")
    void pollFromBufferWhenBufferIsEmpty() {
        assertNull(buffer.pollFromBuffer());
    }
}