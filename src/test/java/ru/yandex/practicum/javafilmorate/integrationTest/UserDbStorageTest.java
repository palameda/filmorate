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
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.UserDbStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @BeforeEach
    void createUserData() {
        if (userDbStorage.findAll().size() != 2) {
            User firstUser = new User("email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"));
            userDbStorage.add(firstUser);
            User secontUser = new User("email@gmail.com", "Login2", "Name2", LocalDate.parse("1980-01-01"));
            userDbStorage.add(secontUser);
        }
        userDbStorage.deleteFriends(1, 2);
    }

    @Test
    @DisplayName("Проверка метода update для User")
    void testUpdateUser() {
        User updateUser = new User("update@yandex.ru",
                "updateLogin",
                "updateName",
                LocalDate.parse("1990-01-01"));
        updateUser.setId(1);
        userDbStorage.update(updateUser);
        Optional<User> userFromDb = userDbStorage.findById(1);
        Map<String, Object> check = new HashMap<>();
        check.put("id", updateUser.getId());
        check.put("email", updateUser.getEmail());
        check.put("login", updateUser.getLogin());
        check.put("name", updateUser.getName());
        check.put("birthday", updateUser.getBirthday());
        for (Map.Entry<String, Object> entry : check.entrySet()) {
            assertThat(userFromDb)
                    .isPresent()
                    .hasValueSatisfying(user ->
                            assertThat(user).hasFieldOrPropertyWithValue(entry.getKey(), entry.getValue())
                    );
        }
    }

    @Test
    @DisplayName("Проверка метода findById для User")
    void testFindUserById() {
        Optional<User> userById = userDbStorage.findById(1);
        assertThat(userById)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @DisplayName("Проверка метода findAll() для User")
    void testFindAll() {
        List<User> currentList = userDbStorage.findAll();
        assertEquals(2, currentList.size(), "Количество пользователей не совпадает");
    }

    @Test
    @DisplayName("Проверка метода addFriendRequest для User")
    void testAddFriendRequest() {
        Assertions.assertTrue(userDbStorage.addFriendRequest(1, 2), "Запрос на дружбу не отправлен");
        Assertions.assertFalse(userDbStorage.addFriendRequest(1, 2), "Запрос на дружбу не должен быть отправлен");
    }

    @Test
    @DisplayName("Проверка метода findAllFriends для User")
    void testFindAllFriends() {
        userDbStorage.addFriendRequest(1, 2);
        List<Integer> firstUserFriends = userDbStorage.findAllFriends(1);
        assertEquals(1, firstUserFriends.size(), "В списке друзей должен быть 1 друг");
        assertEquals(2, (int) firstUserFriends.get(0), "Значение ID друга должно быть равно 2");
        List<Integer> secondUserFriends = userDbStorage.findAllFriends(2);
        assertEquals(0, secondUserFriends.size(), "Список друзей должен быть пуст");
    }

    @Test
    @DisplayName("Проверка метода deleteFriends для User")
    void testDeleteFriends() {
        userDbStorage.addFriendRequest(1, 2);
        Assertions.assertTrue(userDbStorage.deleteFriends(1, 2), "Запрос на дружбу не был удален");
        Assertions.assertFalse(userDbStorage.deleteFriends(1, 2), "Запрос на дружбу не должен быть удален");
    }
}