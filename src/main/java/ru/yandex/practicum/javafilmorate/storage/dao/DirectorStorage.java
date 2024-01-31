package ru.yandex.practicum.javafilmorate.storage.dao;

import ru.yandex.practicum.javafilmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {

    List<Director> findAll();

    Director findById(Integer directorId);

    Integer addDirector(Director director);

    void updateDirector(Director director);

    void deleteDirector(Integer directorId);

    Set<Director> findDirectorsByFilmId(Integer filmId);

    void addDirectorsToFilm(Integer filmId, List<Director> directors);

    void removeDirectorsFromFilm(Integer filmId);
}
