package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.javafilmorate.storage.dao.LikeStorage;
import ru.yandex.practicum.javafilmorate.utils.CheckUtil;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public Film findById(Integer filmId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение фильма по id {}", filmId);
        return filmStorage.findById(filmId);
    }

    public Film addFilm(Film film) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление фильма с id {}", film.getId());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на обновление фильма с id {}", film.getId());
        return filmStorage.updateFilm(film);
    }

    public List<Film> findAll() {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка фильмов");
        return filmStorage.findAll();
    }

    public void addLike(Integer filmId, Integer userId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на добавление отметки \"like\" " +
                "фильму с id {} от пользователя с id {} ", filmId, userId);
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на удаление отметки \"like\" " +
                "фильму с id {} от пользователя с id {} ", filmId, userId);
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer limit) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка {} самых популярных фильмов", limit);
        return filmStorage.getPopularFilms(limit);
    }

    public void deleteFilm(int filmId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на удаление фильма с Id={}.", filmId);
        CheckUtil.checkNotFound(filmStorage.deleteFilm(filmId), " фильм с Id=" + filmId);
    }

    public List<Film> commonFilms(int userId, int friendId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка общих фильмов пользователя {} " +
                "и его друга {}.", userId, friendId);
        return filmStorage.commonFilms(userId, friendId);
    }

    public List<Film> getPopularByGenre(int count, int genreId) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка {} самых популярных фильмов в жанре " +
                "{}.", count, genreId);
        return filmStorage.getPopularByGenre(count, genreId);
    }

    public List<Film> getPopularByYear(int count, int year) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка {} самых популярных фильмов," +
                " выпущенных в {} году.", count, year);
        return filmStorage.getPopularByYear(count, year);
    }

    public List<Film> getPopularByGenreAndYear(int count, int genreId, int year) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка {} самых популярных фильмов в жанре " +
                "{}, выпущенных в {} году.", count, genreId, year);
        return filmStorage.getPopularByGenreAndYear(count, genreId, year);
    }

    public List<Film> searchBySubstring(String query, String by) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка всех фильмов, содержащих строку {}", query);
        return filmStorage.searchBySubstring(query, by);
    }

    public List<Film> findDirectorFilmsByYearOrLikes(int directorId, String sortBy) {
        log.info("СЕРВИС: Отправлен запрос к хранилищу на получение списка фильмов режиссера с id {}, " +
                "отсортированных по {}", directorId, sortBy);
        return filmStorage.findDirectorFilmsByYearOrLikes(directorId, sortBy);
    }
}
