package ru.yandex.practicum.javafilmorate.storage.dao;

import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getUserFriends(Integer userId);

    List<User> getCommonsFriends(int userId, int friendId);
}
