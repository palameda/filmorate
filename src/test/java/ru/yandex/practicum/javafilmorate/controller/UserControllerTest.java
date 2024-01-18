package ru.yandex.practicum.javafilmorate.controller;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.UserService;
import ru.yandex.practicum.javafilmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

class UserControllerTest {
    private UserController controller;
    private static Validator validator;

    @BeforeAll
    public static void beforeAll() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    public void initializeController() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    @DisplayName("Создание пользователя с корректными данными")
    public void testUserCreatedSuccessfully() {
        int initialId = 1;
        User user = controller.createUser(new User("username@company.com",
                "blacksmith", "John Smith", LocalDate.of(1970, 1, 1)));

        List<User> users = controller.findAll();

        Assertions.assertEquals(initialId, user.getId());
        Assertions.assertEquals(user, users.get(0)); // + проверка метода findAll при GET-запросе
    }

    @Test
    @DisplayName("Создание пользователя с некорректным email")
    public void testAttemptToCreateUserWithInvalidEmail() {
        Set<ConstraintViolation<User>> violation = validator.validate(new User("username.company.com@",
                        "blacksmith", "", LocalDate.of(1970, 1, 1)));
        Assertions.assertFalse(violation.isEmpty());
    }

    @Test
    @DisplayName("Создание пользователя с некорректным логином (пустая строка/пробел/null")
    public void testAttemptToCreateUserWithIncorrectLogin() {
        // 1 случай - пустая строка
        Set<ConstraintViolation<User>> blankViolation = validator.validate(new User("username@company.com",
                "", "John Smith", LocalDate.of(1970, 1, 1)));
        Assertions.assertFalse(blankViolation.isEmpty());
        // 2 случай - логин содержит только пробел(ы)
        Set<ConstraintViolation<User>> spaceViolation = validator.validate(new User("username@company.com",
                " ", "John Smith", LocalDate.of(1970, 1, 1)));
        Assertions.assertFalse(spaceViolation.isEmpty());
        // 3 случай - логин null
        Set<ConstraintViolation<User>> nullViolation = validator.validate(new User("username@company.com",
                null, "John Smith", LocalDate.of(1970, 1, 1)));
        Assertions.assertFalse(nullViolation.isEmpty());
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void testAttemptToCreateUserWithBlankName() {
        User user = controller.createUser(new User("username@companyname.com",
                "blacksmith", "", LocalDate.of(1970, 1, 1)));
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    @DisplayName("Создание пользователя с именем = null")
    public void testAttemptToCreateUserWithNullName() {
        User user = controller.createUser(new User("username@companyname.com",
                "blacksmith", null, LocalDate.of(1970, 1, 1)));
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    @DisplayName("Создание пользователя с некорректной датой рождения (future date)")
    public void testAttemptToCreateUserWithIncorrectBirthDate() {
        Set<ConstraintViolation<User>> futureDateViolation = validator.validate(new User("username@company.com",
                "blacksmith", "John Smith", LocalDate.of(2070, 1, 1)));
        Assertions.assertFalse(futureDateViolation.isEmpty());
    }

    @Test
    @DisplayName("Корректное обновление пользователя")
    public void testAttemptToUpdateRegisteredUser() {
        User user = new User("username@company.com",
                "blacksmith", "John Smith", LocalDate.of(1970, 1, 1));
        controller.createUser(user);

        User changedUser = new User("username@organization.com",
                "blacksmith", "John Smith", LocalDate.of(1970, 1, 1));
        changedUser.setId(user.getId());
        controller.updateUser(changedUser);

        List<User> users = controller.findAll();
        Assertions.assertEquals(changedUser, users.get(0));
    }

    @Test
    @DisplayName("Обновление пользователя с некорректным id")
    public void testAttemptToUpdateUserWithIncorrectID() {
        User user = new User("username@company.com",
                "blacksmith", "John Smith", LocalDate.of(1970, 1, 1));
        controller.createUser(user);

        User changedUser = new User("username@company.com",
                "blacksmith", "John Smith", LocalDate.of(1970, 1, 1));
        changedUser.setId(0);

        UnregisteredDataException exception = Assertions.assertThrows(
                UnregisteredDataException.class,
                () -> controller.updateUser(changedUser)
        );
        Assertions.assertEquals("Пользователь не зарегистрирован в системе", exception.getMessage());
    }

    @Test
    @DisplayName("Генерация исключения при обновлении пользователя с логином, содержащим пробел")
    public void testAttemptToUpdateUserWithLoginWithSpace() {
        User user = new User("username@company.com",
                "blacksmith", "John Smith", LocalDate.of(1970, 1, 1));
        controller.createUser(user);

        User changedUser = new User("username@company.com",
                "new login", "John Smith", LocalDate.of(1970, 1, 1));
        changedUser.setId(user.getId());

        InvalidDataException exception = Assertions.assertThrows(
                InvalidDataException.class,
                () -> controller.updateUser(changedUser)
        );
        Assertions.assertEquals("login пользователя не должен содержать пробелы", exception.getMessage());
    }
}