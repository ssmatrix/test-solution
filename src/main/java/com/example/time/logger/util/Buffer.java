package com.example.time.logger.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@UtilityClass
public class Buffer {

    private static final ConcurrentLinkedQueue<LocalDateTime> bufferQueue = new ConcurrentLinkedQueue<>();

    public static void addToBuffer(LocalDateTime timestamp) {
        log.info("Buffer: add " + timestamp);
        bufferQueue.add(timestamp);
    }

    public static boolean isEmpty() {
        log.info("Buffer: is empty " + bufferQueue.isEmpty());
        return bufferQueue.isEmpty();
    }

    public static LocalDateTime pollFromBuffer() {
        log.info("Buffer: poll " + bufferQueue.peek());
        return bufferQueue.poll();
    }
}