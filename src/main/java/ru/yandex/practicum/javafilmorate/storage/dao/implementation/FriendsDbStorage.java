package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.FriendStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component()
public class FriendsDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int userId, int friendId) {
        isRegistered(userId);
        isRegistered(friendId);
        String sqlQuery = "MERGE INTO FRIENDS(USER_ID, FRIEND_ID) VALUES (?, ? )";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Пользователь с id {} получил запрос дружбы от пользователя с id {}", userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        isRegistered(userId);
        isRegistered(friendId);
        String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Пользователь с id {} удалил из друзей пользователя с id {}", userId, friendId);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        isRegistered(userId);
        log.info("Получение друзей пользователя с id {}", userId);
        String sqlQuery = "SELECT USERS.* FROM FRIENDS" +
                " JOIN USERS ON FRIENDS.FRIEND_ID = USERS.USER_ID WHERE FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("LOGIN"),
                        rs.getString("NAME"),
                        rs.getDate("BIRTHDAY").toLocalDate(),
                        null),
                userId
        );
    }

    @Override
    public List<User> getCommonsFriends(int userId, int friendId) {
        isRegistered(userId);
        isRegistered(friendId);
        log.info("Получение общих друзей для пользователей с id {} и {}", userId, friendId);
        String sqlQuery = " SELECT * FROM USERS WHERE USER_ID IN ((SELECT TBL1.FRIEND_ID " +
                " FROM (SELECT USER_ID, FRIEND_ID FROM FRIENDS WHERE USER_ID = ?) AS TBL1 " +
                " INNER JOIN (SELECT USER_ID, FRIEND_ID FROM FRIENDS WHERE USER_ID = ?) AS TBL2 " +
                " ON TBL1.FRIEND_ID = TBL2.FRIEND_ID)) ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("LOGIN"),
                        rs.getString("NAME"),
                        rs.getDate("BIRTHDAY").toLocalDate(),
                        null),
                userId, friendId);
    }

    private void isRegistered(int userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        if (!userRow.next()) {
            throw new UnregisteredDataException("Пользователь с id " + userId + " не зарегистрирован в системе");
        }
    }
}
