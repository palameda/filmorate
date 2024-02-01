package ru.yandex.practicum.javafilmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    boolean deleteUser(int userId);

    User findById(Integer userId);

    List<User> findAll();
}
