package ru.yandex.practicum.javafilmorate.storage.dao.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        log.info("Добавление фильма {} в хранилище", film.getName());
        return simpleJdbcInsert.executeAndReturnKey(film.filmRowMap()).intValue();
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
                "FILM_NAME = ?, MPA_ID = ?, FILM_DESCRIPTION = ? , FILM_RELEASE_DATE = ?, FILM_DURATION = ?, FILM_RATE = ?" +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getMpa().getId(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating(),
                film.getId()
        );
        log.info("Обновление фильма {} в хранилище", film.getName());
    }

    @Override
    public Optional<Film> findById(int id) {
        String sqlQuery = "SELECT * FROM FILMS JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID WHERE FILMS.FILM_ID = ? ";
        var result = jdbcTemplate.queryForObject(sqlQuery, this::rowMapper, id);
        log.info("Получение фильма c id {}",id);
        return Optional.of(result);
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT * FROM FILMS JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID";
        log.info("Получение списка фильмов");
        return jdbcTemplate.query(sqlQuery, this::rowMapper);
    }

    @Override
    public boolean setGenre(int filmId, int genreId) {
        if (!findFilmGenres(filmId, genreId)) {
            String sqlQuery = String.format("INSERT INTO FILM_GENRES VALUES (%d, %d)", filmId, genreId);
            return jdbcTemplate.update(sqlQuery) == 1;
        }
        log.info("Добавление жанра к фильму");
        return true;
    }

    @Override
    public boolean deleteGenre(int filmId, int genreId) {
        if (findFilmGenres(filmId, genreId)) {
            String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ? AND GENRE_ID = ?";
            return jdbcTemplate.update(sqlQuery, filmId, genreId) > 0;
        }
        log.info("Удаление жанра из фильма");
        return false;
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        log.info("Пользователю с id {} понравился фильм с {}", userId, filmId);
        if (!findUserLike(filmId, userId)) {
            String sqlQuery = String.format("INSERT INTO LIKES VALUES (%d, %d)", filmId, userId);
            return jdbcTemplate.update(sqlQuery) == 1;
        }
        return false;
    }

    @Override
    public List<Film> mostPopulars(int limit) {
        List<Film> allFilms = findAll();
        for (Film film : allFilms) {
            String sqlQueryFindLikes = String.format("SELECT COUNT(*) FROM LIKES WHERE FILM_ID = %d", film.getId());
            List<Integer> countLikes = jdbcTemplate.queryForList(sqlQueryFindLikes, Integer.class);
            film.setLikes(film.getRating() + countLikes.get(0));
            String sqlQueryUpdateFilm = "UPDATE FILMS SET FILM_LIKES = ? WHERE FILM_ID = ?";
            jdbcTemplate.update(
                    sqlQueryUpdateFilm,
                    film.getLikes(),
                    film.getId()
            );
        }
        List<Film> mostPopularFilms = new ArrayList<>();
        String sqlQuery = String.format("SELECT FILM_ID FROM FILMS ORDER BY FILM_LIKES DESC LIMIT %d", limit);
        List<Integer> filmIds = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        if (filmIds.isEmpty()) {
            throw new InvalidDataException("Список с популярными фильмами пустой");
        }
        for (Integer id : filmIds) {
            mostPopularFilms.add(findById(id)
                    .orElseThrow(() -> new InvalidDataException("Фильм не найден в системе")));
        }
        log.info("Получение списка из {} самых популярных фильмов", limit);
        return mostPopularFilms;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        log.info("Пользователю с id {} больше не нравится фильм с id {}", userId, filmId);
        if (findUserLike(filmId, userId)) {
            String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
            return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
        }
        return false;
    }

    private Film rowMapper(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film(
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("FILM_RELEASE_DATE").toLocalDate(),
                rs.getInt("FILM_DURATION"),
                rs.getInt("FILM_RATE"),
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")),
                new ArrayList<>()
        );
        film.setId(rs.getInt("FILM_ID"));
        film.setLikes(getFilmLikes(film.getId()));
        return film;
    }

    private Integer getFilmLikes(Integer filmId) {
        String sqlQuery = String.format("SELECT FILM_LIKES FROM FILMS WHERE FILM_ID = %d", filmId);
        List<Integer> countLikes = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        if (countLikes.size() > 0) {
            return countLikes.get(0);
        } else {
            return 0;
        }
    }

    private boolean findUserLike(Integer filmId, Integer userId) {
        String sqlQuery = String.format("SELECT COUNT(*) FROM LIKES WHERE FILM_ID = %d AND USER_ID = %d", filmId, userId);
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class) == 1;
    }

    private boolean findFilmGenres(Integer filmId, Integer genreId) {
        String sqlQuery = String.format("SELECT COUNT(*) FROM FILM_GENRES WHERE FILM_ID = %d AND GENRE_ID = %d", filmId, genreId);
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class) == 1;
    }
}
