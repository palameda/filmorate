package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.dao.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getById(int genreId) {
        log.info("Отправлен запрос к хранилищу на получение жанра с id {}", genreId);
        return genreStorage.findById(genreId);
    }

    public List<Genre> getAll() {
        log.info("Отправлен запрос к хранилищу на получение списка рейтингов");
        return genreStorage.findAll();
    }
}
