package ru.yandex.practicum.javafilmorate.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataExcepion;

import java.util.Map;
import java.util.HashMap;
import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;

/**
 * Класс UserController работает с endpoint'ом "/users" и обслуживает пользователей сервиса Filmorate.
 * Выполняет обработку POST, PUT и GET -запросов.
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int ID = 1;
    private final Map<Integer, User> users = new HashMap<>();

    /**
     * Метод createUser обрабатывает POST-запрос и добавляет пользователя в систему.<br>
     * В случае некорректных данных генерируется исключение InvalidDataException.
     * @param user
     * @return user
     */
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            log.error("При выполнении POST-запроса передан login пользователя, содержащий пробел(ы)");
            throw new InvalidDataExcepion("login пользователя не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setID(ID);
        users.put(user.getID(), user);
        ID++;
        log.info("Пользователь {} ({}) успешно добавлен", user.getLogin(), user.getName());
        return user;
    }

    /**
     * Метод updateUser обрабатывает PUT-запрос и обновляет данные пользователя в системе.<br>
     * В случае передачи некорректных данных генерируется исключение InvalidDataException.
     * @param user
     * @return user
     */
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getID())) {
            log.error("При выполнении PUT-запроса передан пользователь, который не зарегистрирован в системе");
            throw new InvalidDataExcepion("Пользователь не зарегистрирован в системе");
        }
        if (user.getLogin().contains(" ")) {
            log.error("При выполнении PUT-запроса передан login пользователя, содержащий пробел(ы)");
            throw new InvalidDataExcepion("login пользователя не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getID(), user);
        log.info("Данные пользователя {} ({}) успешно обновлены", user.getLogin(), user.getName());
        return user;
    }

    /**
     * Метод findAll обрабатывает GET-запрос и возвращает список пользователей, зарегистрированных в системе
     * @return ArrayList<>(users)
     */
    @GetMapping
    public List<User> findAll() {
        log.info("При выполнении GET-запроса получено пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }
}