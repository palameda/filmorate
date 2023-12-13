package ru.yandex.practicum.javafilmorate.utils;

public class UnregisteredDataException extends RuntimeException {
    public UnregisteredDataException(String message) {
        super(message);
    }
}
