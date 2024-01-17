package ru.yandex.practicum.javafilmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Genre;

import java.util.List;

@Component
public interface GenreStorage {

    List<Genre> getGenres(int filmId);

    String findById(int id);

    List<Genre> findAll();
}
