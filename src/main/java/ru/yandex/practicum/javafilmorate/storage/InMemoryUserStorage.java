package ru.yandex.practicum.javafilmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User addUser(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return new HashMap<>(users);
    }
}
