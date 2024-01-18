package ru.yandex.practicum.javafilmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.JavaFilmorateApplication;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.MpaDbStorage;

import java.util.LinkedList;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;
    @Test
    @DisplayName("Проверка получения MPA по id")
    void testFindNameMpa() {
        LinkedList<String> nameMpa = new LinkedList<>();
        nameMpa.add("G");
        nameMpa.add("PG");
        nameMpa.add("PG-13");
        nameMpa.add("R");
        nameMpa.add("NC-17");
        for (int i = 1; i < nameMpa.size()-1; i++) {
            Assertions.assertEquals(mpaDbStorage.findById(i), nameMpa.get(i-1), "Названия рейтингов не совпадают");
        }
    }

    @Test
    void testFindAll() {
        Assertions.assertEquals(5, mpaDbStorage.findAll().size(), "Размер не соответсвует");
    }
}
