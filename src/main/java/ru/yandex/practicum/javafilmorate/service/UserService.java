package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.UserStorage;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImplementation {
    private final UserStorage userStorage;

    /**Добавление пользователя.*/
    public User add(User user) {
        user.setId(userStorage.add(user));
        return user;
    }

    /**Обновление пользователя.*/
    public void update(User user) {
        getById(user.getId());
        userStorage.update(user);
    }

    /**Получение списка всех пользователей.*/
    public List<User> getAll() {
        return userStorage.findAll();
    }

    /**Добавление друзей.*/
    public boolean addFriend(Integer userId, Integer friendId) {
        getById(userId);
        getById(friendId);
        return userStorage.addFriendRequest(userId, friendId);
    }

    /**Удаление друзей.*/
    public void deleteFriend(Integer userId, Integer friendId) {
        getById(userId);
        getById(friendId);
        if (!userStorage.deleteFriends(userId, friendId)) {
            throw new InvalidDataException("Не удалось удалить пользователя из друзей");
        }
    }

    /**Вывод списка друзей*/
    public List<User> getUserFriends(Integer userId) {
        getById(userId);
        List<Integer> idFriends = userStorage.findAllFriends(userId);
        List<User> friends = new ArrayList<>();
        for (Integer friendId : idFriends) {
            friends.add(getById(friendId));
        }
        return friends;
    }

    /**Вывод списка общих друзей*/
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        getById(userId);
        getById(friendId);
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> common = new HashSet<>(userStorage.findAllFriends(userId));
        common.retainAll(userStorage.findAllFriends(friendId));
        for (Integer id : common) {
            commonFriends.add(getById(id));
        }
        return commonFriends;
    }

    /**Получение пользователя по id.*/
    public User getById(Integer id) {
        return userStorage.findById(id).orElseThrow(
                () -> new InvalidDataException("Пользователь с такими данным не зарегестрирован в системе")
        );
    }
}
