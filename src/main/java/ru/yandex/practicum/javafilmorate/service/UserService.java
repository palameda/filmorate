package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.UserStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        user.setId(userStorage.add(user));
        log.info("Пользователь с именем {} успешно добавлен", user.getName());
        return user;
    }

    public void update(User user) {
        getById(user.getId());
        userStorage.update(user);
        log.info("Пользователь с id {} успешно обновлён", user.getId());
    }

    public List<User> getAll() {
        log.info("Список всех пользователей получен");
        return userStorage.findAll();
    }

    public boolean addFriend(Integer userId, Integer friendId) {
        getById(userId);
        getById(friendId);
        log.info("Запрос на дружбу оправлен");
        return userStorage.addFriendRequest(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        getById(userId);
        getById(friendId);
        log.info("Друг удалён");
        if (!userStorage.deleteFriends(userId, friendId)) {
            throw new UnregisteredDataException("Не удалось удалить пользователя из друзей");
        }
    }

    public List<User> getUserFriends(Integer userId) {
        getById(userId);
        List<Integer> idFriends = userStorage.findAllFriends(userId);
        List<User> friends = new ArrayList<>();
        for (Integer friendId : idFriends) {
            friends.add(getById(friendId));
        }
        log.info("Получен список друзей пользователя с id {}", userId);
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        getById(userId);
        getById(friendId);
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> common = new HashSet<>(userStorage.findAllFriends(userId));
        common.retainAll(userStorage.findAllFriends(friendId));
        for (Integer id : common) {
            commonFriends.add(getById(id));
        }
        log.info("Получен список общих друзей");
        return commonFriends;
    }

    public User getById(Integer id) {
        if (id > 0 && id < 1000) {
            log.info("Пользователь по id {} успешно получен", id);
            return userStorage.findById(id).orElseThrow(
                    () -> new UnregisteredDataException("Пользователь с такими данным не зарегестрирован в системе")
            );
        } else {
            throw new UnregisteredDataException("Пользователь с такими данным не зарегестрирован в системе");
        }
    }
}
