package ru.yandex.practicum.javafilmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.JavaFilmorateApplication;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.GenreDbStorage;

import java.util.ArrayList;



@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    @DisplayName("Проверка метода findById для Genre")
    void testFindGenreById() {
        ArrayList<String> genres = new ArrayList<>();
        genres.add("Комедия");
        genres.add("Драма");
        genres.add("Мультфильм");
        genres.add("Триллер");
        genres.add("Документальный");
        genres.add("Боевик");
        for (int i = 1; i < genres.size() - 1; i++) {
            Assertions.assertEquals(genreDbStorage.findById(i), genres.get(i - 1), "Названия жанров не совпадают");
        }
    }

    @Test
    @DisplayName("Проверка метода findAll() для Genre")
    void testFindAllGenres() {
        Assertions.assertEquals(6, genreDbStorage.findAll().size(), "Количество данных не совпадает");
    }
}