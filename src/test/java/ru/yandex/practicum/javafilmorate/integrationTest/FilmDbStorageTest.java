package ru.yandex.practicum.javafilmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.javafilmorate.JavaFilmorateApplication;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.*;

import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    final GenreDbStorage genreStorage;
    final LikesDbStorage likesDbStorage;
    final UserDbStorage userDbStorage;
    private int film1Id, film2Id, film3Id;
    private int user1Id, user2Id, user3Id;

    @BeforeEach
    void createFilmData() {
        Film film1 = new Film(null, "Film1", "Description1", LocalDate.parse("1970-01-01"),
                140, new Mpa(1, "G"), 0);
        filmStorage.addFilm(film1);
        film1Id = film1.getId();

        Film film2 = new Film(null, "Film2", "Description2", LocalDate.parse("1980-01-01"),
                90, new Mpa(2, "PG"), 0);
        filmStorage.addFilm(film2);
        film2Id = film2.getId();

        Film film3 = new Film(null, "Film3", "Description3", LocalDate.parse("1990-01-01"),
                190, new Mpa(2, "PG"), 0);
        filmStorage.addFilm(film3);
        film3Id = film3.getId();

        User firstUser = new User(1, "email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"), null);
        userDbStorage.addUser(firstUser);
        user1Id = firstUser.getId();
        User secontUser = new User(1, "email@gmail.com", "Login2", "Name2", LocalDate.parse("1980-01-01"), null);
        userDbStorage.addUser(secontUser);
        user2Id = secontUser.getId();
        User thirdUser = new User(3, "email@gmail.com", "Login3", "Name3", LocalDate.parse("1990-01-01"), null);
        userDbStorage.addUser(thirdUser);
        user3Id = thirdUser.getId();
    }

    @Test
    @DisplayName("Проверка метода update для Film")
    void testUpdateFilm() {
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
        Assertions.assertEquals(3, current.size(), "Количество фильмов не совпадает");
    }

    @Test
    @DisplayName("Проверка метода commonFilms для Film")
    void testCommonFilms() {
        likesDbStorage.addLike(film1Id, user1Id);
        likesDbStorage.addLike(film2Id, user1Id);
        likesDbStorage.addLike(film3Id, user1Id);

        likesDbStorage.addLike(film1Id, user2Id);
        likesDbStorage.addLike(film2Id, user2Id);

        likesDbStorage.addLike(film2Id, user3Id);

        /*Проверяем размер полученного списка*/
        List<Film> current = filmStorage.commonFilms(user1Id, user2Id);
        Assertions.assertEquals(2, current.size(), "Количество фильмов не совпадает");

        /*Проверяем порядок элементов в списке.*/
        /*Первым должен быть фильм с id=2 т.к. у него три лайка*/
        Assertions.assertEquals(2, current.get(0).getId());
        Assertions.assertEquals(1, current.get(1).getId());
    }
}
