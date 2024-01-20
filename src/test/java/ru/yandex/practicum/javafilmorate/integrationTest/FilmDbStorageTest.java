package ru.yandex.practicum.javafilmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.JavaFilmorateApplication;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.FilmDbStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.GenreDbStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    final FilmDbStorage filmStorage;
    final GenreDbStorage genreStorage;
    final UserDbStorage userDbStorage;

    @BeforeEach
    void createFilmData() {
        if (filmStorage.findAll().size() != 2) {
            Set<Genre> genres = new HashSet<>();
            genres.add(new Genre(2, genreStorage.findById(2).getName()));

            Film film1 = new Film(null,"Film1", "Description1", LocalDate.parse("1970-01-01"),
                    140, new Mpa(1, "G"), 0);
            filmStorage.addFilm(film1);

            Film film2 = new Film(null, "Film2", "Description2", LocalDate.parse("1980-01-01"),
                    90, new Mpa(2, "PG"), 0);
            filmStorage.addFilm(film2);
        }
        if (userDbStorage.findAll().size() != 2) {
            User firstUser = new User(1, "email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"), null);
            userDbStorage.addUser(firstUser);
            User secontUser = new User(1, "email@gmail.com", "Login2", "Name2", LocalDate.parse("1980-01-01"), null);
            userDbStorage.addUser(secontUser);
        }
    }

    @Test
    @DisplayName("Проверка метода update для Film")
    void testUpdateFilm() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(2, genreStorage.findById(2).getName()));
        Film updateFilm = new Film(1, "Film1", "updateDescription", LocalDate.parse("1990-01-01"), 140, new Mpa(1, "G"), 0);
        filmStorage.updateFilm(updateFilm);
        Film afterUpdate = filmStorage.findById(1);
        Assertions.assertEquals(afterUpdate.getDescription(), "updateDescription");
    }

    @Test
    @DisplayName("Проверка метода findById для Film")
    void testFindFilmById() {
        Film film = filmStorage.findById(1);
        Assertions.assertEquals(film.getId(), 1);
    }

    @Test
    @DisplayName("Проверка метода findAll() для Film")
    void testFindAll() {
        List<Film> current = filmStorage.findAll();
        Assertions.assertEquals(2, current.size(), "Количество фильмов не совпадает");
    }
}
