package com.example.time.logger.buffer;

import java.time.LocalDateTime;


public interface Buffer {
    void addToBuffer(LocalDateTime timestamp);

    boolean isEmpty();

    LocalDateTime pollFromBuffer();

    void setRecoveryInProgress(boolean flag);

    boolean isRecoveryInProgress();

    void executeInLock(Runnable action);
}
