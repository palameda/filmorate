package ru.yandex.practicum.javafilmorate.storage.dao.implementation;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.dao.GenreStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres(int filmId) {
        List<Genre> genres = new ArrayList<>();
        String sqlQuery = String.format("SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = %d", filmId);
        List<Integer> genreIds = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        for (Integer id : genreIds) {
            genres.add(new Genre(id, findById(id)));
        }
        log.info("Получение жанров для фильма");
        return genres;
    }

    @Override
    public String findById(int id) {
        String sqlQuery = String.format("SELECT GENRE_NAME FROM GENRES WHERE GENRE_ID = %d", id);
        List<String> genreNames = jdbcTemplate.queryForList(sqlQuery, String.class);
        if (genreNames.size() != 1) {
            throw new UnregisteredDataException("Передан некорректный id жанра");
        }
        log.info("Получение жанра по id {}", id);
        return genreNames.get(0);
    }

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT GENRE_ID, GENRE_NAME FROM GENRES";
        log.info("Получение списка жанров");
        return jdbcTemplate.query(sqlQuery, this::rowMapper);
    }

    private Genre rowMapper(ResultSet rs, int rowNum) throws SQLException {
        log.info("Производится маппинг жанра");
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}
