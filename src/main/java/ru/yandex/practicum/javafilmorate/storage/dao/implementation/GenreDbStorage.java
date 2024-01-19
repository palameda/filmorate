package ru.yandex.practicum.javafilmorate.storage.dao.implementation;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.dao.GenreStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findById(int genreId) {
        log.info("Получение жанра по id {}", genreId);
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE GENRE_ID = ?", genreId);
        if (rs.next()) {
            return new Genre(
                    rs.getInt("GENRE_ID"),
                    rs.getString("GENRE_NAME")
            );
        } else {
            throw new UnregisteredDataException("Жанр с id " + genreId + " не зарегистрирован в системе");
        }
    }

    @Override
    public List<Genre> findAll() {
        log.info("Получение из хранилища списка всех жанров");
        List<Genre> genres = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES");
        while (rs.next()) {
            Genre genre = new Genre(
                    rs.getInt("GENRE_ID"),
                    rs.getString("GENRE_NAME")
            );
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public void reloadGenres(Film film) {
        log.info("Перезапись-обновление жанров для фильма с {}", film.getName());
        deleteFilmGenre(film);
        addFilmGenre(film);
    }

    @Override
    public void deleteFilmGenre(Film film) {
        log.info("Удаление жанров для фильма {}", film.getName());
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", film.getId());
    }

    @Override
    public void addFilmGenre(Film film) {
        List<Genre> genres = List.copyOf(film.getGenres());
        if (!genres.isEmpty()) {
            String sqlQuery = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genres.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        }
//        if (film.getGenres() != null) {
//            for (Genre genre : film.getGenres()) {
//                jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
//                        film.getId(), genre.getId());
//            }
//        }
    }
}
