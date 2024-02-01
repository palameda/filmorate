package ru.yandex.practicum.javafilmorate.utils;

public final class CheckUtil {
    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new UnregisteredDataException("В Базе не найден " + msg);
        }
    }
}
