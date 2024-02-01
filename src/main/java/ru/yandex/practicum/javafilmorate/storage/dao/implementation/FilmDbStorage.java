package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.GenreStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.MpaStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Component()
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public List<Film> findAll() {
        log.info("ХРАНИЛИЩЕ: Получение из хранилища списка всех фильмов");
        String sqlQuery = "SELECT * FROM FILMS";
        List<Film> films = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rs.next()) {
            films.add(filmRowMap(rs));
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        log.info("ХРАНИЛИЩЕ: Добавление фильма с id {} в хранилище", film.getId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.filmToMap()).intValue());
        genreStorage.addFilmGenre(film);
        return findById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("ХРАНИЛИЩЕ: Обновление данных по фильму с id {}", film.getId());
        if (getAllFilms(film.getId()).next()) {
            String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?, " +
                    "FILM_DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            mpaStorage.findById(film.getMpa().getId());
            deleteFilmGenres(film.getId());
            fillFilmGenres(film);
            film.setGenres(getFilmGenres(film.getId()));
            return film;
        } else {
            throw new UnregisteredDataException("Фильм с id " + film.getId() + " не зарегистрирован в системе");
        }
    }

    @Override
    public boolean deleteFilm(int filmId) {
        log.info("ХРАНИЛИЩЕ: Удаление из хранилища фильма с id {}", filmId);
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ? ";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    @Override
    public Film findById(int filmId) {
        log.info("ХРАНИЛИЩЕ: Получение фильма по id {}", filmId);
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (rs.next()) {
            return filmRowMap(rs);
        } else {
            throw new UnregisteredDataException("Фильм с id " + filmId + " не зарегистрирован в системе");
        }
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        List<Film> films = new ArrayList<>();
        String sqlQuery = "SELECT F.*, COUNT(L.ID) FROM FILMS AS F " +
                "LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID ORDER BY COUNT(L.ID) DESC LIMIT ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, limit);
        System.out.println(rs);
        while (rs.next()) {
            films.add(filmRowMap(rs));
        }
        log.info("ХРАНИЛИЩЕ: Получение списка {} самых популярных фильмов", limit);
        return films;
    }

    private Film filmRowMap(SqlRowSet rs) {
        log.info("ХРАНИЛИЩЕ: Производится маппинг фильма");
        Film film = new Film(
                rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("FILM_RELEASE_DATE").toLocalDate(),
                rs.getInt("FILM_DURATION"),
                mpaStorage.findById(rs.getInt("MPA_ID")),
                getFilmLikes(rs.getInt("FILM_ID")));
        film.setGenres(getFilmGenres(film.getId()));
        return film;
    }

    private Genre genreRowMap(SqlRowSet rs) {
        log.info("ХРАНИЛИЩЕ: Производится маппинг жанра");
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

    private int getFilmLikes(int filmId) {
        log.info("ХРАНИЛИЩЕ: Получение количества отметок \"лайк\" для фильма с id {}", filmId);
        String sqlQuery = "SELECT COUNT(FILM_ID) AS AMOUNT FROM LIKES WHERE FILM_ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (rs.next()) {
            return rs.getInt("AMOUNT");
        } else {
            return 0;
        }
    }

    private Set<Genre> getFilmGenres(int filmId) {
        Set<Genre> filmGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID IN " +
                "(SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        while (rs.next()) {
            filmGenres.add(genreRowMap(rs));
        }
        log.info("ХРАНИЛИЩЕ: Получение жарнов для фильма с id {}", filmId);
        return filmGenres;
    }

    private SqlRowSet getAllFilms(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        log.info("ХРАНИЛИЩЕ: Получение rowSet фильма с id {}", filmId);
        return jdbcTemplate.queryForRowSet(sqlQuery, filmId);
    }

    private void fillFilmGenres(Film film) {
        log.info("ХРАНИЛИЩЕ: Производится добавление жанов для фильма {}", film.getName());
        isGenreRegistered(film);
        List<Genre> genres = List.copyOf(film.getGenres());
        String sqlQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
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

    private void deleteFilmGenres(int filmId) {
        log.info("ХРАНИЛИЩЕ: Производится удаление жанров у фильма с id {}", filmId);
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private void isGenreRegistered(Film film) {
        log.info("ХРАНИЛИЩЕ: Проверка жанров фильма с id {} на существование", film.getId());
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        for (Genre genre : film.getGenres()) {
            if (!jdbcTemplate.queryForRowSet(sqlQuery, genre.getId()).next()) {
                throw new UnregisteredDataException("Жанр с id " + genre.getId() + " не зарегистрирован в системе");
            }
        }
    }

    @Override
    public List<Film> commonFilms(int userId, int friendId) {
        log.info("ХРАНИЛИЩЕ: Получение списка общих фильмов пользователя id={} " +
                " и его друга id={} отсортированных по популярности.", userId, friendId);

        String sqlQuery = "SELECT t.*        \n" +
                "            FROM (SELECT f.*, \n" +
                "                         count(f.film_id) likes\n" +
                "                    FROM Films f\n" +
                "                   INNER JOIN LIKES l ON l.film_id = f.film_id  \n" +
                "                   GROUP BY (f.film_id)) t\n" +
                "           INNER JOIN LIKES l2 ON l2.film_id = t.film_id AND l2.user_id=?\n" +
                "          INTERSECT\n" +
                "          SELECT t.*        \n" +
                "            FROM (SELECT f.*, \n" +
                "                         count(f.film_id) likes\n" +
                "                    FROM Films f\n" +
                "                   INNER JOIN LIKES l ON l.film_id = f.film_id  \n" +
                "                   GROUP BY (f.film_id)) t\n" +
                "           INNER JOIN LIKES l2 ON l2.film_id = t.film_id AND l2.user_id=? \n" +
                "          ORDER BY likes DESC; ";

        List<Film> films = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        while (rs.next()) {
            films.add(filmRowMap(rs));
        }

        return films;
    }


}
