package ru.yandex.practicum.javafilmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.service.FilmService;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private static final LocalDate VALID_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        return new ResponseEntity<>(filmService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        return new ResponseEntity<>(filmService.getPopularFilms(count), HttpStatus.OK);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateDate(film);
        return filmService.add(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        validateDate(film);
        filmService.update(film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void validateDate(Film film) {
        if (film.getReleaseDate().isBefore(VALID_DATE)) {
            throw new InvalidDataException("Дата релиза не может быть раньше 28 декабря 1895");
        }
    }
}