package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;
import ru.yandex.practicum.javafilmorate.utils.InvalidDataException;
import ru.yandex.practicum.javafilmorate.utils.UnregisteredDataException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class FilmServiceImplementation implements FilmService {
    private static final LocalDate FIRST_PUBLIC_SCREENING_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;

    @Override
    public Film addFilm(Film film) {
        checkFilmDate(film);
        log.info("Фильм {} успешно добавлен", film.getName());
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        checkFilmRegistration(film.getId());
        checkFilmDate(film);
        filmStorage.updateFilm(film);
        log.info("Данные о фильме {} успешно обновлены.", film.getName());
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        checkFilmRegistration(film.getId());
        filmStorage.deleteFilm(film);
        log.info("Данные о фильме {} успешно удалены.", film.getName());
    }

    @Override
    public Film getFilmById(int id) {
        Film film = checkFilmRegistration(id);
        log.info("Фильм c id {} зарегестрирован в системе как {}", id, film.getName());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = List.copyOf(filmStorage.getAllFilms().values());
        log.info("В системе зарегистриовано {} фильмов", films.size());
        return films;
    }

    @Override
    public void addLike(int id, int userId) {
        Film film = checkFilmRegistration(id);
        film.getLikes().add(userId);
        log.info("Фильм {} понравился пользователю с id {}", film.getName(), userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        Film film = checkFilmRegistration(id);
        if (!film.getLikes().contains(userId)) {
            throw new UnregisteredDataException("Пользователь не найден в системе");
        }
        film.getLikes().remove(userId);
        log.info("Пользователю с id {} больше не нравиться фильм {}", userId, film.getName());
    }

    @Override
    public List<Film> getPopularFilms(String count) {
        int records;
        try {
            records = Integer.parseInt(count);
        } catch (NumberFormatException exception) {
            records = 5;
        }
        List<Film> films = filmStorage.getAllFilms().values().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(records)
                .collect(Collectors.toList());
        return films;
    }

    private Film checkFilmRegistration(int id) {
        if (!filmStorage.getAllFilms().containsKey(id)) {
            log.error("Переданы данные о фильме, который не зарегистрирован в системе");
            throw new UnregisteredDataException("Данные о фильме отсутствуют в системе");
        }
        return filmStorage.getAllFilms().get(id);
    }

    private void checkFilmDate(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_PUBLIC_SCREENING_DATE)) {
            log.error("При выполнении PUT-запроса передан фильм с датой {}, которая предшествует 28.12.1895 г.",
                    film.getReleaseDate());
            throw new InvalidDataException("Дата фильма не должна предшествовать 28.12.1895 г.");
        }
    }
}
