package ru.yandex.practicum.javafilmorate.utils;

/**
 * InvalidDataException - runtime-исключение, которое генерируется в случае обработки некорректных данных
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}

