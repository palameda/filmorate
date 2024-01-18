package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaService mpaService;
    private final GenreService genreService;

    public Film getById(Integer filmId) {
        if (filmId < 9900) {
            Film film = filmStorage.findById(filmId).orElseThrow(
                    () -> new UnregisteredDataException("Фильм не зарегестрирован в системе")
            );
            film.setGenres(genreService.getFilmGenres(film.getId()));
            log.info("Получен фильм с id {}", film);
            return film;
        } else {
            throw new UnregisteredDataException("Фильм не зарегестрирован в системе");
        }
    }

    public Film add(Film film) {
        film.setId(filmStorage.add(film));
        film.setMpa(mpaService.getById(film.getMpa().getId()));
        List<Genre> genres = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            genres.add(genreService.getById(genre.getId()));
            if (!filmStorage.setGenre(film.getId(), genre.getId())) {
                throw new UnregisteredDataException("Добавить жанр фильму не удалось");
            }
        }
        film.setGenres(genres);
        log.info("Фильм {} добавлен в хранилище", film.getName());
        return film;
    }

    public void update(Film film) {
        getById(film.getId());
        filmStorage.update(film);
        film.setMpa(mpaService.getById(film.getMpa().getId()));
        List<Genre> genres = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            if (!genres.contains(genreService.getById(genre.getId()))) {
                genres.add(genreService.getById(genre.getId()));
            }
            if (!filmStorage.setGenre(film.getId(), genre.getId())) {
                throw new UnregisteredDataException("Изменить жанр фильму не удалось");
            }
        }
        List<Genre> filmGenres = genreService.getFilmGenres(film.getId());
        for (Genre genre : filmGenres) {
            if (!genres.contains(genre)) {
                filmStorage.deleteGenre(film.getId(), genre.getId());
            }
        }
        film.setGenres(genres);
        log.info("Фильм {} обновлён", film.getName());
    }

    public List<Film> getAll() {
        List<Film> films = filmStorage.findAll();
        for (Film film : films) {
            film.setGenres(genreService.getFilmGenres(film.getId()));
        }
        log.info("Список фильмов получен");
        return films;
    }

    public void addLike(Integer filmId, Integer userId) {
        validateId(userId);
        if (!filmStorage.addLike(filmId, userId)) {
            throw new UnregisteredDataException("Не удалось поставить отметку \"нравится\"");
        }
    }

    public void deleteLike(Integer filmId, Integer userId) {
        validateId(userId);
        if (!filmStorage.deleteLike(filmId, userId)) {
            throw new UnregisteredDataException("Не удалось удалить отметку \"нравится\"");
        }
    }

    public List<Film> getPopularFilms(Integer limit) {
        log.info("Получен список из {} самых популярных фильмов", limit);
        return filmStorage.mostPopulars(limit);
    }

    private void validateId(Integer userId) {
        if (userId < 1) {
            throw new UnregisteredDataException("id пользователя не может быть отрицательным");
        }
    }
}
