package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Director;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.dao.DirectorStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Component
public class DirectorDbStorage implements DirectorStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> findAll() {
        log.info("ХРАНИЛИЩЕ: Получение из хранилища списка всех режиссёров");
        String sqlQuery = "SELECT * FROM DIRECTORS";
        List<Director> directors = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rs.next()) {
            directors.add(directorRowMap(rs));
        }
        return directors;
    }

    @Override
    public Director findById(Integer directorId) {
        log.info("ХРАНИЛИЩЕ: Получение режиссёра по id {}", directorId);
        String sqlQuery = "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, directorId);
        if (rs.next()) {
            return directorRowMap(rs);
        } else {
            throw new UnregisteredDataException("Режиссёр с id " + directorId + " не зарегистрирован в системе");
        }
    }

    @Override
    public Director addDirector(Director director) {
        log.info("ХРАНИЛИЩЕ: Добавление режиссёра с id {} в хранилище", director.getId());
        if (director == null) {
            throw new UnregisteredDataException("При добавлении режиссёра был передан null");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("DIRECTORS")
                .usingGeneratedKeyColumns("DIRECTOR_ID");
        director.setId(simpleJdbcInsert.executeAndReturnKey(director.directorToMap()).intValue());
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        log.info("ХРАНИЛИЩЕ: Обновление режиссёра с id {}", director.getId());
        if (findById(director.getId()) == null) {
            throw new UnregisteredDataException("При обновлении данных передан режиссёр без id");
        }
        String sqlQuery = "UPDATE DIRECTORS SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return director;
    }

    @Override
    public void deleteDirector(Integer directorId) {
        log.info("ХРАНИЛИЩЕ: Удаление режиссёра с id {}", directorId);
        String sqlQuery = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sqlQuery, directorId);
    }

    @Override
    public Set<Director> findDirectorsByFilmId(Integer filmId) {
        log.info("ХРАНИЛИЩЕ: Поиск режиссёров для фильма с id {}", filmId);
        Set<Director> directors = new HashSet<>();
        String sqlQuery = "SELECT D.* FROM FILMS_DIRECTORS AS FD " +
                "JOIN DIRECTORS AS D ON FD.DIRECTOR_ID = D.DIRECTOR_ID " +
                "WHERE FD.FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        while (rs.next()) {
            directors.add(directorRowMap(rs));
        }
        return directors;
    }

    @Override
    public void addFilmDirectors(Film film) {
        log.info("ХРАНИЛИЩЕ: Добавление режиссёров для фильма с id {}", film.getId());
        List<Director> directors = List.copyOf(film.getDirectors());
        if (!directors.isEmpty()) {
            String sqlQuery = "INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, directors.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return directors.size();
                }
            });
        }
    }

    @Override
    public void deleteFilmDirectors(Film film) {
        log.info("ХРАНИЛИЩЕ: Удаление режиссёров для фильма с id {}", film.getId());
        String sqlQuery = "DELETE FROM FILMS_DIRECTORS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    public Director directorRowMap(SqlRowSet rs) {
        log.info("ХРАНИЛИЩЕ: Производится маппинг режиссёра");
        return new Director(
             rs.getInt("DIRECTOR_ID"),
             rs.getString("DIRECTOR_NAME")
        );
    }
}
