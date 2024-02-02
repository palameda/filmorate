package ru.yandex.practicum.javafilmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films/{}", id);
        return filmService.findById(id);
    }

    @GetMapping(value = "/popular", params = "genreId")
    public List<Film> getPopularByGenre(@RequestParam(value = "count", defaultValue = "10") Integer count,
                                        @RequestParam Integer genreId) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films/popular params = {count, genreId}");
        return filmService.getPopularByGenre(count, genreId);
    }

    @GetMapping(value = "/popular", params = "year")
    public List<Film> getPopularByYear(@RequestParam(defaultValue = "10") Integer count,
                                       @RequestParam Integer year) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films/popular params = {count, year}");
        return filmService.getPopularByYear(count, year);
    }

    @GetMapping(value = "/popular", params = {"genreId", "year"})
    public List<Film> getPopularByGenreAndYear(@RequestParam(defaultValue = "10") Integer count,
                                               @RequestParam Integer genreId,
                                               @RequestParam Integer year) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films/popular params = {count, genreId, year}");
        return filmService.getPopularByGenreAndYear(count, genreId, year);
    }

    @GetMapping(value = "/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films/popular");
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/search")
    public List<Film> searchBySubstring(@RequestParam String query, @RequestParam String by) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films/search");
        return filmService.searchBySubstring(query, by);
    }
    @GetMapping
    public List<Film> findAll() {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films");
        return filmService.findAll();
    }

    @GetMapping("/director/{directorId}")
    public List<Film> findDirectorFilmsByYearOrLikes(@PathVariable Integer directorId, @RequestParam String sortBy) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films/director/{directorId}");
        return filmService.findDirectorFilmsByYearOrLikes(directorId, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("КОНТРОЛЛЕР: POST-запрос по эндпоинту /films");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("КОНТРОЛЛЕР: PUT-запрос по эндпоинту /films");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("КОНТРОЛЛЕР: PUT-запрос по эндпоинту /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("КОНТРОЛЛЕР: DELETE-запрос по эндпоинту /films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable int filmId) {
        log.info("КОНТРОЛЛЕР: DELETE-запрос по эндпоинту /{}", filmId);
        filmService.deleteFilm(filmId);
    }

    @GetMapping("/common")
    public List<Film> commonFilms(@RequestParam int userId, @RequestParam int friendId) {
        log.info("КОНТРОЛЛЕР: GET-запрос по эндпоинту /films/common?userId={}&friendId={}", userId, friendId);
        return filmService.commonFilms(userId, friendId);
    }
}