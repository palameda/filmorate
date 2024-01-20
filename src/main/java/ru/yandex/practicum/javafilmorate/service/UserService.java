package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.FriendStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.UserStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User addUser(User user) {
        log.info("Отправлен запрос к хранилищу на добавление пользователя с id {}", user.getId());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Отправлен запрос к хранилищу на обновление пользователя с id {}", user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> findAll() {
        log.info("Отправлен запрос к хранилищу на получение списка всех пользователей");
        return userStorage.findAll();
    }

    public void addFriend(Integer userId, Integer friendId) {
        log.info("Отправлен запрос к хранилищу на добавление пользователю с id {} друга с id {}", userId, friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        log.info("Отправлен запрос к хранилищу на удаление у пользователя с id {} друга с id {}", userId, friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getUserFriends(Integer userId) {
        log.info("Отправлен запрос к хранилищу на получение списка всех друзей пользователя с id {}", userId);
        return friendStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        log.info("Отправлен запрос к хранилищу на получение списка общих друзей пользователей с id {} и id {}", userId, friendId);
        return friendStorage.getCommonsFriends(userId, friendId);
    }

    public User findById(Integer userId) {
        log.info("Отправлен запрос к хранилищу на получение пользователя по id {}", userId);
        return userStorage.findById(userId);
    }
}
