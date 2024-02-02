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
import ru.yandex.practicum.javafilmorate.service.UserService;
import ru.yandex.practicum.javafilmorate.storage.dao.FriendStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.LikeStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.FilmDbStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final FriendStorage friendStorage;
    private final LikeStorage likeStorage;
    private final User firstUser = new User(1, "email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"), null);
    private final User secontUser = new User(1, "email@gmail.com", "Login2", "Name2", LocalDate.parse("1980-01-01"), null);
    private final User thirdUser = new User(3, "email@gmail.com", "Login3", "Name3", LocalDate.parse("1990-01-01"), null);


    @BeforeEach
    void createUserData() {
        userDbStorage.addUser(firstUser);
        userDbStorage.addUser(secontUser);
        userDbStorage.addUser(thirdUser);
    }

    @Test
    @DisplayName("Проверка метода update для User")
    void testUpdateUser() {
        User updateUser = new User(1, "update@yandex.ru",
                "updateLogin",
                "updateName",
                LocalDate.parse("1990-01-01"), null);
        User user = userDbStorage.updateUser(updateUser);
        Assertions.assertEquals(user.getLogin(), "updateLogin", "Данные в полях login() не совпадают");
    }

    @Test
    @DisplayName("Проверка метода findById для User")
    void testFindUserById() {
        User user = userDbStorage.findById(1);
        Assertions.assertEquals(user.getId(), 1, "Данные полей id не совпадают");
    }

    @Test
    @DisplayName("Проверка метода findAll() для User")
    void testFindAll() {
        List<User> currentList = userDbStorage.findAll();
        assertEquals(3, currentList.size(), "Количество пользователей не совпадает");
    }

    @Test
    @DisplayName("Проверка метода deleteUser")
    void testDeleteUser() {
        userDbStorage.deleteUser(2);

        List<User> current = userDbStorage.findAll();
        Assertions.assertEquals(2, current.size(), "Количество user не совпадает.");

        User[] expect = new User[]{firstUser, thirdUser};
        Assertions.assertArrayEquals(expect, current.toArray(), "Удален не тот user.");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    @DisplayName("Проверка метода findSimilarUserId в UserService")
    void findRecommendationsForUserTest() {
        UserService userService = new UserService(userDbStorage, filmDbStorage, friendStorage);

        Film film1 = new Film(null, "Film1", "Description1", LocalDate.parse("1970-01-01"),
                140, new Mpa(1, "G"), 0);
        filmDbStorage.addFilm(film1);
        int film1Id = film1.getId();
        Film film2 = new Film(null, "Film2", "Description2", LocalDate.parse("1980-01-01"),
                90, new Mpa(2, "PG"), 0);
        filmDbStorage.addFilm(film2);
        int film2Id = film2.getId();
        Film film3 = new Film(null, "Film3", "Description3", LocalDate.parse("1990-01-01"),
                190, new Mpa(2, "PG"), 0);
        filmDbStorage.addFilm(film3);
        int film3Id = film3.getId();
        Film film4 = new Film(null, "Film4", "Description4", LocalDate.parse("1980-01-01"),
                190, new Mpa(2, "PG"), 0);
        filmDbStorage.addFilm(film4);
        int film4Id = film4.getId();
        Film film5 = new Film(null, "Film5", "Description5", LocalDate.parse("1985-01-01"),
                190, new Mpa(2, "PG"), 0);
        filmDbStorage.addFilm(film5);
        int film5Id = film5.getId();

        int user1Id = firstUser.getId();
        int user2Id = secontUser.getId();
        int user3Id = thirdUser.getId();

        likeStorage.addLike(film1Id, user1Id);
        likeStorage.addLike(film2Id, user1Id);
        likeStorage.addLike(film3Id, user1Id);
        likeStorage.addLike(film5Id, user1Id);

        likeStorage.addLike(film1Id, user2Id);
        likeStorage.addLike(film2Id, user2Id);

        likeStorage.addLike(film1Id, user3Id);
        likeStorage.addLike(film4Id, user3Id);
        /* Рекомендация фильмов должна состоять из списка одного фильма с Id 3*/
        List<Film> films = userService.findRecommendationsForUser(user2Id);
        assertThat(films.size()).isEqualTo(2);
        assertThat(films.get(0).getId()).isEqualTo(3);
        assertThat(films.get(1).getId()).isEqualTo(5);
    }
}