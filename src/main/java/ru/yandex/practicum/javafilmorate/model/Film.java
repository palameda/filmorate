package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Размер описания не должен превышать 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = "Продолжительнось фильма должна быть больше 0")
    private final int duration;
    private int rating;
    private Mpa mpa;
    private List<Genre> genres;
    private int likes;

    public Film(String name, String description, LocalDate releaseDate,
                int duration, int rate, Mpa mpa, List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = Objects.requireNonNullElse(rate, 0);
        this.mpa = mpa;
        this.genres = Objects.requireNonNullElseGet(genres, ArrayList::new);
    }

    public Map<String, Object> filmRowMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_NAME", name);
        values.put("FILM_DESCRIPTION", description);
        values.put("FILM_RELEASE_DATE", releaseDate);
        values.put("FILM_DURATION", duration);
        values.put("FILM_RATE", rating);
        values.put("MPA_ID", mpa.getId());
        values.put("FILM_LIKES", likes);
        return values;
    }
}