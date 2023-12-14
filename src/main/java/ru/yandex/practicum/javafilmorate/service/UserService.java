package ru.yandex.practicum.javafilmorate.service;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    User getUserById(int id);

    List<User> getAllUsers();

    void addFriend(int selfId, int friendId);

    void deleteFriend(int selfId, int friendId);

    List<User> getUserFriends(int selfId);

    List<User> getCommonFriends(int firstId, int secondId);
}
