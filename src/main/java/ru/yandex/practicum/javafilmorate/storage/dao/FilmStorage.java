package ru.yandex.practicum.javafilmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component
public interface FilmStorage {
    Integer add(Film film);

    void update(Film film);

    Optional<Film> findById(int id);

    List<Film> findAll();

    boolean setGenre(int filmId, int genreId);

    boolean deleteGenre(int filmId, int genreId);

    boolean addLike(int filmId, int userId);

    List<Film> mostPopulars(int limit);

    boolean deleteLike(int filmId, int userId);
}
