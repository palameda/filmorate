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
import ru.yandex.practicum.javafilmorate.model.User;;
import ru.yandex.practicum.javafilmorate.storage.dao.implementation.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = JavaFilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
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

}