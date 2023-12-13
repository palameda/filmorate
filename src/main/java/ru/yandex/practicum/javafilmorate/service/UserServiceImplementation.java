package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.UserStorage;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserStorage userStorage;

    @Override
    public User addUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("При добавлении пользователя передан login, содержащий пробел(ы)");
            throw new InvalidDataException("login пользователя не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.addUser(user);
        log.info("Пользователь {} ({}) успешно добавлен", user.getLogin(), user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!userStorage.getAllUsers().containsKey(user.getId())) {
            log.error("Пользователь не зарегистрирован в системе");
            throw new InvalidDataException("Пользователь не зарегистрирован в системе");
        }
        if (user.getLogin().contains(" ")) {
            log.error("При обновлении пользователя передан login, содержащий пробел(ы)");
            throw new InvalidDataException("login пользователя не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.updateUser(user);
        log.info("Данные пользователя {} ({}) успешно обновлены", user.getLogin(), user.getName());
        return user;
    }

    @Override
    public void deleteUser(User user) {
        log.info("Пользователь {} ({}) успешно удалён", user.getLogin(), user.getName());
        userStorage.deleteUser(user);
    }

    @Override
    public User getUserById(int id) {
        if (!userStorage.getAllUsers().containsKey(id)) {
            log.error("Пользователь не зарегистрирован в системе");
            throw new InvalidDataException("Пользователь не зарегистрирован в системе");
        }
        log.info("Пользователь c id {} зарегестрирован в системе", id);
        return userStorage.getAllUsers().get(id);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Зарегестрированные в системе пользователи возвращены");
        return List.copyOf(userStorage.getAllUsers().values());
    }

    @Override
    public void addFriend(int selfId, int friendId) {

    }

    @Override
    public void deleteFriend(int selfId, int friendId) {

    }

    @Override
    public List<User> getUserFriends(int selfId) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(int firstId, int secondId) {
        return null;
    }
}
