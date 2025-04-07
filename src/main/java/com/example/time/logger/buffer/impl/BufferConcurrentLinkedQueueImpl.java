package com.example.time.logger.buffer.impl;


import com.example.time.logger.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class BufferConcurrentLinkedQueueImpl implements Buffer {

    private static final ConcurrentLinkedQueue<LocalDateTime> bufferQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void addToBuffer(LocalDateTime timestamp) {
        log.info("Buffer: add " + timestamp);
        bufferQueue.add(timestamp);
    }

    @Override
    public boolean isEmpty() {
        log.info("Buffer: is empty " + bufferQueue.isEmpty());
        return bufferQueue.isEmpty();
    }

    @Override
    public LocalDateTime pollFromBuffer() {
        log.info("Buffer: poll " + bufferQueue.peek());
        return bufferQueue.poll();
    }
}
