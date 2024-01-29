package ru.yandex.practicum.javafilmorate.storage.dao;

import ru.yandex.practicum.javafilmorate.model.Director;

import java.util.List;

public interface DirectorStorage {

    List<Director> findAll();

    Director findById(Integer directorId);

    Integer addDirector(Director director);

    void updateDirector(Director director);

    void deleteDirector(Integer directorId);

}
