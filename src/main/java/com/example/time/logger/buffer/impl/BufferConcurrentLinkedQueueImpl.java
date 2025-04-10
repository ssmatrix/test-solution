package com.example.time.logger.buffer.impl;


import com.example.time.logger.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class BufferConcurrentLinkedQueueImpl implements Buffer {


    private final ConcurrentLinkedQueue<LocalDateTime> bufferQueue = new ConcurrentLinkedQueue<>();
    private final Set<LocalDateTime> bufferedTimestamps = ConcurrentHashMap.newKeySet();
    private final ReentrantLock lock = new ReentrantLock();
    private volatile boolean recoveryInProgress = false;

    @Override
    public void addToBuffer(LocalDateTime timestamp) {
        lock.lock();
        try {
            if (bufferedTimestamps.add(timestamp)) {
                log.info("Buffer: add " + timestamp);
                bufferQueue.add(timestamp);
            } else {
                log.debug("Buffer: skip duplicate " + timestamp);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        return bufferQueue.isEmpty();
    }

    @Override
    public LocalDateTime pollFromBuffer() {
        lock.lock();
        try {
            LocalDateTime timestamp = bufferQueue.poll();
            if (timestamp != null) {
                bufferedTimestamps.remove(timestamp);
                log.info("Buffer: poll " + timestamp);
            }
            return timestamp;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setRecoveryInProgress(boolean flag) {
        this.recoveryInProgress = flag;
    }

    @Override
    public boolean isRecoveryInProgress() {
        return recoveryInProgress;
    }

    @Override
    public void executeInLock(Runnable action) {
        lock.lock();
        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }
}

