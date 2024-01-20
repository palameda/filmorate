package ru.yandex.practicum.javafilmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }
}