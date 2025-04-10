package com.example.time.logger.buffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

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
        buffer.setRecoveryInProgress(false);
    }

    @Test
    @Description("Check that addToBuffer adds a new element to the buffer.")
    void addToBufferShouldAddNewElement() {
        var dateTime = LocalDateTime.now();
        buffer.addToBuffer(dateTime);

        assertFalse(buffer.isEmpty());
        assertEquals(dateTime, buffer.pollFromBuffer());
    }

    @Test
    @Description("Check that duplicate values are not added.")
    void addToBufferShouldIgnoreDuplicates() {
        var dateTime = LocalDateTime.now();
        buffer.addToBuffer(dateTime);
        buffer.addToBuffer(dateTime);

        assertFalse(buffer.isEmpty());
        assertEquals(dateTime, buffer.pollFromBuffer());
        assertNull(buffer.pollFromBuffer());
    }

    @Test
    @Description("Check that isEmpty returns true when the buffer is empty.")
    void isEmptyWhenBufferIsEmpty() {
        assertTrue(buffer.isEmpty());
    }

    @Test
    @Description("Check that isEmpty returns false when buffer is not empty.")
    void isEmptyWhenBufferNotEmpty() {
        buffer.addToBuffer(LocalDateTime.now());
        assertFalse(buffer.isEmpty());
    }

    @Test
    @Description("Check that pollFromBuffer returns the last element added.")
    void pollFromBufferShouldReturnLastElement() {
        var dateTime = LocalDateTime.now();
        buffer.addToBuffer(dateTime);

        var polledDateTime = buffer.pollFromBuffer();
        assertEquals(dateTime, polledDateTime);
    }

    @Test
    @Description("Check that pollFromBuffer returns null when buffer is empty.")
    void pollFromBufferWhenBufferIsEmpty() {
        assertNull(buffer.pollFromBuffer());
    }

    @Test
    @Description("Check that executeInLock executes the runnable inside a lock.")
    void executeInLockShouldExecuteCode() {
        AtomicBoolean executed = new AtomicBoolean(false);

        buffer.executeInLock(() -> executed.set(true));

        assertTrue(executed.get());
    }

    @Test
    @Description("Check that setRecoveryInProgress and isRecoveryInProgress work.")
    void recoveryInProgressFlagWorksCorrectly() {
        assertFalse(buffer.isRecoveryInProgress());
        buffer.setRecoveryInProgress(true);
        assertTrue(buffer.isRecoveryInProgress());
    }
}