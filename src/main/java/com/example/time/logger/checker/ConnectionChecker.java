package com.example.time.logger.checker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
@AllArgsConstructor
public class ConnectionChecker {

    private final DataSource dataSource;

    public boolean isConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            log.error("No connection. ");
            return false;
        }
    }
}