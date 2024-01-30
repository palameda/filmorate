package ru.yandex.practicum.javafilmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {
    List<Film> findAll();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int filmId);

    Film findById(int filmId);

    List<Film> getPopularFilms(int limit);

    List<Film> commonFilms(int userId, int friendId);

}
