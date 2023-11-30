package ru.practicum.javafilmorate.utils;

/**
 * InvalidDataException - runtime-исключение, которое генерируется в случае обработки некорректных данных
 */
public class InvalidDataExcepion extends RuntimeException {
    public InvalidDataExcepion(String message) {
        super(message);
    }
}
