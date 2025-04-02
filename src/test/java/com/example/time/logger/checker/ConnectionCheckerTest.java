package com.example.time.logger.checker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectionCheckerTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private ConnectionChecker connectionChecker;

    @Test
    @Description("check that the isConnection method returns true if the database is available.")
    void isConnectionShouldReturnTrue() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);

        var actual = connectionChecker.isConnection();

        assertTrue(actual);

        verify(dataSource, times(1)).getConnection();
        verify(connection, times(1)).isClosed();
        verify(connection).close();
    }

    @Test
    @Description("check that the isConnection method returns false if the connection to the database is closed.")
    void isConnectionShouldReturnFalse() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(true);

        var actual = connectionChecker.isConnection();

        assertFalse(actual);

        verify(dataSource, times(1)).getConnection();
        verify(connection, times(1)).isClosed();
        verify(connection).close();
    }

    @Test
    @Description("check that the isConnection method returns false if an exception occurs while obtaining a connection.")
    void isConnectionShouldReturnException() throws Exception {
        when(dataSource.getConnection()).thenThrow(new RuntimeException("Database not available"));

        var actual = connectionChecker.isConnection();

        assertFalse(actual);

        verify(dataSource, times(1)).getConnection();
        verify(connection, never()).isClosed();
    }
}