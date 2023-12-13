package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUnregisteredDataException(final UnregisteredDataException exception) {
        log.info("404 {}", exception.getMessage());
        return Map.of("errorMessage", exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidDataException(final InvalidDataException exception) {
        log.info("400 {}", exception.getMessage());
        return Map.of("errorMessage", exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnknownException(final Exception exception) {
        log.info("500 {}", exception.getMessage());
        return Map.of("errorMessage", exception.getMessage());
    }
}
