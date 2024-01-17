package ru.yandex.practicum.javafilmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {
    Integer add(User user);

    void update(User user);

    Optional<User> findById(int userId);

    List<User> findAll();

    boolean addFriendRequest(int userId, int friendId);

    List<Integer> findAllFriends(int userId);

    boolean deleteFriends(int userId, int friendId);
}
