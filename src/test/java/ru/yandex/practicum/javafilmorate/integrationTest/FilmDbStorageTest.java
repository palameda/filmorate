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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    final FilmDbStorage filmDbStorage;
    final GenreDbStorage genreDbStorage;
    final UserDbStorage userDbStorage;

    @BeforeEach
    void createFilmData() {
        if (filmDbStorage.findAll().size() != 2) {
            List<Genre> genres = new ArrayList<>();
            genres.add(new Genre(2, genreDbStorage.findById(2)));

            Film film1 = new Film("Film1", "Description1", LocalDate.parse("1970-01-01"),
                    140, 4, new Mpa(1, "G"), genres);
            filmDbStorage.add(film1);
            filmDbStorage.setGenre(1, 2);

            Film film2 = new Film("Film2", "Description2", LocalDate.parse("1980-01-01"),
                    90, 0, new Mpa(2, "PG"), genres);
            filmDbStorage.add(film2);
            filmDbStorage.setGenre(2, 2);
        }
        if (userDbStorage.findAll().size() != 2) {
            User firstUser = new User("email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"));
            userDbStorage.add(firstUser);
            User secontUser = new User("email@gmail.com", "Login2", "Name2", LocalDate.parse("1980-01-01"));
            userDbStorage.add(secontUser);
        }
    }

    @Test
    @DisplayName("Проверка метода update для Film")
    void testUpdateFilm() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(2, genreDbStorage.findById(2)));
        Film updateFilm = new Film("Film1", "updateDescription", LocalDate.parse("1990-01-01"), 140, 4, new Mpa(1, "G"), genres);
        updateFilm.setId(1);
        filmDbStorage.update(updateFilm);
        Optional<Film> optionalFilm = filmDbStorage.findById(1);
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "updateDescription")
                );
    }

    @Test
    @DisplayName("Проверка метода findById для Film")
    void testFindFilmById() {
        Optional<Film> optionalFilm = filmDbStorage.findById(1);
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @DisplayName("Проверка метода findAll() для Film")
    void testFindAll() {
        List<Film> current = filmDbStorage.findAll();
        Assertions.assertEquals(2, current.size(), "Количество фильмов не совпадает");
    }

    @Test
    @DisplayName("Проверка метода deleteGenre для Film")
    void testDeleteGenre() {
        Assertions.assertTrue(filmDbStorage.deleteGenre(2, 2), "Жанр фильма не изменился");
        List<Genre> genres = new ArrayList<>();
        Optional<Film> filmDbStorageFilm = filmDbStorage.findById(2);
        assertThat(filmDbStorageFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", genres)
                );
        filmDbStorage.setGenre(2, 2);
    }

    @Test
    @DisplayName("Проверка метода addLike для Film")
    void testAddLike() {
        Assertions.assertTrue(filmDbStorage.addLike(2, 1), "Лайк не добавлен");
        filmDbStorage.deleteLike(2, 1);
    }

    @Test
    @DisplayName("Проверка метода deleteLike для Film")
    void testDeleteLike() {
        filmDbStorage.addLike(1, 1);
        Assertions.assertTrue(filmDbStorage.deleteLike(1, 1), "Лайк не удален");
    }

    @Test
    @DisplayName("Проверка метода mostPopulars для Film")
    void testMostPopularFilms() {
        filmDbStorage.addLike(1, 1);
        List<Film> films = filmDbStorage.mostPopulars(1);
        Assertions.assertEquals(1, films.size(), "Количество фильмов не совпадает");
        Optional<Film> filmDbStorageFilm = filmDbStorage.findById(1);
        assertThat(filmDbStorageFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("Likes", film.getRating() + 1)
                );
        films = filmDbStorage.mostPopulars(2);
        Assertions.assertEquals(2, films.size(), "Количество фильмов не совпадает");
        filmDbStorageFilm = filmDbStorage.findById(2);
        assertThat(filmDbStorageFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("Likes", film.getRating())
                );
    }
}
