package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.storage.dao.LikeStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class LikesDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Integer filmId, Integer userId) {
        isFilmRegistered(filmId);
        isUserRegistered(userId);
        log.info("Сохранение отметки\"like\" фильму с id {} от пользователя с id {}", filmId, userId);
        String sqlQuery = "MERGE INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        isFilmRegistered(filmId);
        isUserRegistered(userId);
        log.info("Удаление отметки\"like\" у фильма с id {} от пользователя с id {}", filmId, userId);
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Integer> getLikes(int filmId) {
        isFilmRegistered(filmId);
        log.info("Получение отметок \"like\" для фильма с id {}", filmId);
        String sqlQuery = "SELECT USER_ID FROM LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("USER_ID"), filmId);
    }

    private void isFilmRegistered(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (!filmRow.next()) {
            throw new UnregisteredDataException("Фильм с id " + filmId + " не зарегистрирован в системе");
        }
    }

    private void isUserRegistered(int userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        if (!userRow.next()) {
            throw new UnregisteredDataException("Пользователь с id " + userId + " не зарегистрирован в системе");
        }
    }
}
