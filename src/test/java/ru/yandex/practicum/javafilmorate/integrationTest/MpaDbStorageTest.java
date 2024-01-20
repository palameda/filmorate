package ru.yandex.practicum.javafilmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.JavaFilmorateApplication;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.MpaDbStorage;

import java.util.ArrayList;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    @DisplayName("Проверка метода findById для Mpa")
    void testFindMpaById() {
        ArrayList<String> mpa = new ArrayList<>();
        mpa.add("G");
        mpa.add("PG");
        mpa.add("PG-13");
        mpa.add("R");
        mpa.add("NC-17");
        for (int i = 1; i < mpa.size() - 1; i++) {
            Assertions.assertEquals(mpaDbStorage.findById(i).getName(), mpa.get(i - 1), "Названия рейтингов не совпадают");
        }
    }

    @Test
    @DisplayName("Проверка метода findAll() для Mpa")
    void testFindAllMpa() {
        Assertions.assertEquals(5, mpaDbStorage.findAll().size(), "Количество данных не совпадает");
    }
}
