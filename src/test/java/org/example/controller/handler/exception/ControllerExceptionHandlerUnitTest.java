package org.example.controller.handler.exception;

import org.example.exception.AppException;
import org.example.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerUnitTest {

    @InjectMocks
    ControllerExceptionHandler controllerExceptionHandler;

    @Mock
    Exception exception;
    @Mock
    AppException appException;
    @Mock
    NotFoundException notFoundException;

    @Test
    void shouldHandleException() {
        controllerExceptionHandler.handleException(exception);

        verify(exception).getMessage();
    }

    @Test
    void shouldHandleAppException() {
        controllerExceptionHandler.handleAppException(appException);

        verify(appException).getMessage();
    }

    @Test
    void shouldHandleNOtFoundException() {
        controllerExceptionHandler.handleNotFoundException(notFoundException);

        verify(notFoundException).getMessage();
    }
}