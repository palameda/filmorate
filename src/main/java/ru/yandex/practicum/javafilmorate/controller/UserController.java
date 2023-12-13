package ru.yandex.practicum.javafilmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.UserService;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Класс UserController работает с endpoint'ом "/users" и обслуживает пользователей сервиса Filmorate.
 * Выполняет обработку GET, POST, PUT и DELETE -запросов.
 */

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Метод findAll обрабатывает GET-запрос и возвращает список пользователей, зарегистрированных в системе
     * @return ArrayList<User>()
     */
    @GetMapping
    public List<User> findAll() {
        return userService.getAllUsers();
    }

    /**
     * Метод getUserById обрабатывает GET-запрос и возвращает пользователя с указанным id
     * @param id
     * @return User
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    /**
     * Метод findAllFriends обрабатывает GET-запрос и возвращает список друзей пользователя с указанным id
     * @param id
     * @return ArrayList<User>()
     */
    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    /**
     * Метод findCommonFriends обрабатывает GET-запрос и возвращает список общих друзей
     * для пользователей с указанными id и otherId
     * @param id
     * @param otherId
     * @return ArrayList<User>()
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    /**
     * Метод createUser обрабатывает POST-запрос и добавляет пользователя в систему.
     * @param user
     * @return user
     */
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Метод addFriend обрабатывает PUT-запрос и делает "друзьями" пользователей с id и friendId
     * @param id
     * @param friendId
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    /**
     * Метод updateUser обрабатывает PUT-запрос и обновляет данные пользователя в системе.<br>
     * В случае передачи некорректных данных генерируется исключение InvalidDataException.
     * @param user
     * @return user
     */
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * Метод deleteFriend обрабатывает DELETE-запрос и удаляет из списка друзей пользователя с id
     * другого пользователя системы с friendId
     * @param id
     * @param friendId
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUnregisteredDataException(final UnregisteredDataException exception) {
        return Map.of("errorMessage", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidDataException(final InvalidDataException exception) {
        return Map.of("errorMessage", exception.getMessage());
    }
}