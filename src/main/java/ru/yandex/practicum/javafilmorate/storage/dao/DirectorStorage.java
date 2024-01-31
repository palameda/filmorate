package ru.yandex.practicum.javafilmorate.storage.dao;

import ru.yandex.practicum.javafilmorate.model.Director;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {

    List<Director> findAll();

    Director findById(Integer directorId);

    Director addDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(Integer directorId);

    Set<Director> findDirectorsByFilmId(Integer filmId);

    void addFilmDirectors(Film film);

    void deleteFilmDirectors(Film film);
}
