package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.dao.GenreStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> getFilmGenres(int filmId) {
        return genreStorage.getGenres(filmId);
    }

    public Genre getById(int id) {
        return new Genre(id, genreStorage.findById(id));
    }

    public List<Genre> getAll() {
        return genreStorage.findAll();
    }
}
