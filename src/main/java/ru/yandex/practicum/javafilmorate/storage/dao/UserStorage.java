package ru.yandex.practicum.javafilmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {
    User add(User user);

    User update(User user);

    User delete(User user);

    User findById(int userId);

    List<User> findAll();
}
