package ru.yandex.practicum.javafilmorate.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataExcepion;

import java.util.Map;
import java.util.HashMap;
import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Класс FilmController работает с endpoint'ом "/films" и обслуживает фильмы сервиса Filmorate.
 * Выполняет обработку POST, PUT и GET -запросов.
 */
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id = 1;
    private static final LocalDate FIRST_PUBLIC_SCREENING_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    /**
     * Метод createFilm обрабатывает POST-запрос и добавляет фильм в систему.<br>
     * В случае некорректных данных генерируется исключение InvalidDataException.
     * @param film
     * @return film
     */
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(FIRST_PUBLIC_SCREENING_DATE)) {
            log.error("При выполнении POST-запроса передан фильм с датой {}, которая предшествует 28.12.1895 г.",
                    film.getReleaseDate());
            throw new InvalidDataExcepion("Дата фильма не должна предшествовать 28.12.1895 г.");
        }
        film.setId(id);
        films.put(film.getId(), film);
        id++;
        log.info("Фильм {} успешно добавлен", film.getName());
        return film;
    }

    /**
     * Метод updateFilm обрабатывает PUT-запрос и обновляет данные о фильме в системе.<br>
     * В случае передачи некорректных данных генерируется исключение InvalidDataException.
     * @param film
     * @return film
     */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("При выполнении PUT-запроса переданы данные о фильме, который не зарегистрирован в системе");
            throw new InvalidDataExcepion("Данные о фильме отсутствуют в системе");
        }
        if (film.getReleaseDate().isBefore(FIRST_PUBLIC_SCREENING_DATE)) {
            log.error("При выполнении PUT-запроса передан фильм с датой {}, которая предшествует 28.12.1895 г.",
                    film.getReleaseDate());
            throw new InvalidDataExcepion("Дата фильма не должна предшествовать 28.12.1895 г.");
        }
        films.put(film.getId(), film);
        log.info("Данные о фильме {} успешно обновлены.", film.getName());
        return film;
    }

    /**
     * Метод findAll обрабатывает GET-запрос и возвращает список фильмов, хранящихся в системе
     * @return ArrayList<>(films)
     */
    @GetMapping
    public List<Film> findAll() {
        log.info("При выполнении GET-запроса получено фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }
}