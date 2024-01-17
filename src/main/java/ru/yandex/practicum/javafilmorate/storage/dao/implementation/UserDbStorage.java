package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.dao.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        return simpleJdbcInsert.executeAndReturnKey(user.userRowMap()).intValue();
    }

    @Override
    public void update(User user) {
        String sqlQuery = "UPDATE USERS SET USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, USER_BIRTHDAY = ?" +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
    }

    @Override
    public Optional<User> findById(int userId) {
        String sqlQuery = "SELECT USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY " +
                "FROM USERS WHERE USER_ID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::rowMapper, userId));
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY FROM USERS";
        return jdbcTemplate.query(sqlQuery, this::rowMapper);
    }

    @Override
    public boolean addFriendRequest(int userId, int friendId) {
        if (!findFriendRequest(userId, friendId)) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("FIRST_USER_ID", userId);
            map.put("SECOND_USER_ID", friendId);
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("FRIENDS")
                    .usingColumns("FIRST_USER_ID", "SECOND_USER_ID");
            return simpleJdbcInsert.execute(map) == 1;
        }
        return false;
    }

    @Override
    public List<Integer> findAllFriends(int userId) {
        String sqlQuery = String.format("SELECT SECOND_USER_ID AS friends " +
                "FROM FRIENDS " +
                "WHERE FIRST_USER_ID = %d", userId);
        return jdbcTemplate.queryForList(sqlQuery, Integer.class);
    }

    @Override
    public boolean deleteFriends(int userId, int friendId) {
        String sqlQuery = String.format("DELETE FROM FRIENDS " +
                "WHERE FIRST_USER_ID = %d AND SECOND_USER_ID = %d", userId, friendId);
        return jdbcTemplate.update(sqlQuery) > 0;
    }

    private User rowMapper(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(
                rs.getString("USER_EMAIL"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("USER_BIRTHDAY").toLocalDate()
        );
        user.setId(rs.getInt("USER_ID"));
        return user;
    }

    private boolean findFriendRequest(int firstUserId, int secondUserId) {
        String sqlQuery = String.format(
                "SELECT COUNT(*) FROM FRIENDS " +
                "WHERE (FIRST_USER_ID = %d OR SECOND_USER_ID = %d)" +
                " AND (FIRST_USER_ID = %d OR SECOND_USER_ID = %d)",
                firstUserId, firstUserId, secondUserId, secondUserId);
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class) == 1;
    }
}