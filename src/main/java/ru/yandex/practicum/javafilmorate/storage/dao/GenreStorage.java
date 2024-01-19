package ru.yandex.practicum.javafilmorate.storage.dao;

import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre findById(int genreId);

    List<Genre> findAll();

    void reloadGenres(Film film);

    void deleteFilmGenre(Film film);

    void addFilmGenre(Film film);
}
