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

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return filmService.findById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") int limit) {
        return filmService.getPopularFilms(limit);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }
}