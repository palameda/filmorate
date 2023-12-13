package ru.yandex.practicum.javafilmorate.service;

import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Film film);

    Film getFilmById(int id);

    List<Film> getFilms();

    void addLike(int id, int userId);

    void deleteLike(int id, int userId);

    List<Film> getPopularFilms(String count);
}
