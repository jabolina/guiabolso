package br.com.jabolina.guiabolso.controller;

import br.com.jabolina.guiabolso.controller.data.ApiResponse;
import br.com.jabolina.guiabolso.exception.GuiaBolsoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = GuiaBolsoException.class)
    public ResponseEntity<ApiResponse> handleApplicationException(GuiaBolsoException ex, WebRequest req) {
        log.error("Application error for {}", req, ex);
        ApiResponse response = ApiResponse.builder()
                .description(ex.getMessage())
                .status(ex.error().statusCode())
                .build();
        return ResponseEntity.status(ex.error().statusCode()).body(response);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleUncaughtException(Exception e, WebRequest req) {
        log.error("Uncaught exception for {}", req, e);
        ApiResponse response = ApiResponse.builder()
                .description(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
