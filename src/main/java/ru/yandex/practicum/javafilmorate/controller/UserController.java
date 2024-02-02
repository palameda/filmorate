package ru.yandex.practicum.javafilmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.EventService;
import ru.yandex.practicum.javafilmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final EventService eventService;

    @GetMapping
    public List<User> findAll() {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /users");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /users/{}", id);
        return userService.findById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /users/{}/friends", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("КОНТРОЛЛЕР: POST-запрос по эндпоинту /users");
        return userService.addUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("КОНТРОЛЛЕР: PUT-запрос по эндпоинту /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("КОНТРОЛЛЕР: PUT-запрос по эндпоинту /users");
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("КОНТРОЛЛЕР: DELETE-запрос по эндпоинту /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("КОНТРОЛЛЕР: DELETE-запрос по эндпоинту /{}", userId);
        userService.deleteUser(userId);
    }


    @GetMapping("/{userId}/recommendations")
    public List<Film> findRecomendationsForUser(@PathVariable("userId") Integer userId) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /{}/recommendations", userId);
        return userService.findRecommendationsForUser(userId);
    }

    @GetMapping("/{userId}/feed")
    public List<Event> getUserFeed(@PathVariable int userId) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /{}/feed", userId);
        return eventService.getUserFeed(userId);
    }
}