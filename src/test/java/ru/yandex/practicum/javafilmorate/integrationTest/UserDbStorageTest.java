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
import ru.yandex.practicum.javafilmorate.storage.dao.FriendStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private final FriendStorage friendStorage;

    @BeforeEach
    void createUserData() {
        if (userDbStorage.findAll().size() != 2) {
            User firstUser = new User(1, "email@yandex.ru", "Login1", "Name1", LocalDate.parse("1970-01-01"), null);
            userDbStorage.addUser(firstUser);
            User secontUser = new User(2, "email@gmail.com", "Login2", "Name2", LocalDate.parse("1980-01-01"), null);
            userDbStorage.addUser(secontUser);
        }
        friendStorage.deleteFriend(1, 2);
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
        assertEquals(2, currentList.size(), "Количество пользователей не совпадает");
    }

}