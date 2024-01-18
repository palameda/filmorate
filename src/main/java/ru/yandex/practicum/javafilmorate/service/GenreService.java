package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.dao.GenreStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> getFilmGenres(int filmId) {
        log.info("Получение жанров для фильма с id {}", filmId);
        return genreStorage.getGenres(filmId);
    }

    public Genre getById(int id) {
        log.info("Получен жанр по id {}", id);
        return new Genre(id, genreStorage.findById(id));
    }

    public List<Genre> getAll() {
        log.info("Получен список жанров");
        return genreStorage.findAll();
    }
}
